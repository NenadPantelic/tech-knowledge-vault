import json

from confluent_kafka import Consumer

from amqp.enrichment_response_producer import EnrichmentResponseProducer
from config.config import config
from data.encrichment_request_event import EnrichmentRequestEvent
from enrichment.enrichment_executor import EnrichmentExecutor
from logger.logger import get_logger

logger = get_logger(__name__)

SUCCESS_EVENT_TYPE = "BookEnrichmentCompleted"
FAILED_EVENT_TYPE = "BookEnrichmentFailed"

ENRICHMENT_STATUS_SUCCESS = "SUCCESS"
ENRICHMENT_STATUS_FAILED = "FAILED"


class EnrichmentRequestConsumer:
    """
    Consumes and processes JSON events from Kafka topics.

    This class encapsulates Kafka consumer initialization, message
    polling, event deserialization, and offset management.

    Messages are committed only after successful processing and
    successful publication of the corresponding result event.

    Attributes:
        _consumer (Consumer): Underlying Confluent Kafka consumer.
    """

    def __init__(self, enrichment_executor: EnrichmentExecutor, producer: EnrichmentResponseProducer):
        """
        Initialize the Kafka event consumer.

        Args:
            enrichment_executor: the executor that will execute the enrichment.
            producer: a Kafka producer that should produce the enrichment result.
        """

        conf = {
            'bootstrap.servers': config.kafka_bootstrap_servers,
            'group.id': config.kafka_consumer_group,
            'auto.offset.reset': config.kafka_auto_offset_reset,
            "enable.auto.commit": False,
            "enable.auto.offset.store": False
        }
        self._consumer = Consumer(conf)
        self._consumer.subscribe([config.kafka_book_requests_topic])
        self._consumer_timeout_in_secs = config.kafka_consumer_timeout_in_secs

        self._enrichment_executor = enrichment_executor
        self._producer = producer

    def process_message(self, message):
        """
        Process the message to enrich the given book.
        :param message:
        :return:
        """
        event = json.loads(message.value().decode("utf-8"))
        enrichment_request_event = EnrichmentRequestEvent.from_dict(event)
        event_id = enrichment_request_event.event_id
        book_id = enrichment_request_event.book.book_id

        logger.info("Processing a message with: event_id = %s, book_id = %s", event_id, book_id)
        try:
            enrichments = self._enrichment_executor.generate_enrichments(enrichment_request_event.book,
                                                                         enrichment_request_event.enrichment)

            result = {
                "event_id": f"result-{event_id}",
                "correlation_id": event_id,
                "event_type": SUCCESS_EVENT_TYPE,
                "book_id": book_id,
                "status": ENRICHMENT_STATUS_SUCCESS,
                "result": enrichments
            }

        except ValueError as exc:
            # Business/validation failure
            result = {
                "event_id": event_id,
                "correlation_id": event_id,
                "event_type": FAILED_EVENT_TYPE,
                "book_id": book_id,
                "status": ENRICHMENT_STATUS_FAILED,
                "reason": str(exc)
            }

        # Publish the outcome before acknowledging
        # the original request.
        self._producer.publish(
            topic=config.kafka_book_results_topic,
            key=str(book_id),
            event=result
        )

        logger.info(
            "Published enrichment result for book %s: %s",
            book_id,
            result["status"]
        )

    def start(self):
        self._consumer.subscribe([
            config.kafka_book_requests_topic
        ])

        logger.info("Book enrichment consumer started")

        try:
            while True:
                message = self._consumer.poll(timeout=1.0)

                if message is None:
                    continue

                if message.error():
                    logger.error("Kafka error: %s", message.error())
                    continue

                try:
                    self.process_message(message)

                    # Commit only after the result has been
                    # successfully delivered to Kafka.
                    self._consumer.commit(
                        message=message,
                        asynchronous=False
                    )

                except Exception:
                    logger.exception(
                        "Processing failed; message will be retried"
                    )

                    # Stop rather than acknowledge a message
                    # whose outcome could not be published.
                    raise

        finally:
            self._consumer.close()
