import json
from logger.logger import get_logger

from confluent_kafka import Producer

from config.config import config

logger = get_logger(__name__)


class EnrichmentResponseProducer:
    """
    Publishes JSON-serialized events to Kafka topics.

    This class encapsulates Kafka producer initialization and
    message delivery, providing a simple interface for publishing
    application events.

    Attributes:
        _producer (Producer): Underlying Confluent Kafka producer.
    """

    def __init__(self):
        self._producer = Producer({
            "bootstrap.servers": ",".join(
                config.kafka_bootstrap_servers
            )
        })

    def publish(self, topic: str, key: str, event: dict):
        """
       Publish an event to a Kafka topic.

       The event is serialized to JSON before being sent.
       The method waits for Kafka to acknowledge message delivery.

       Args:
           topic: Destination Kafka topic.
           key: Kafka message key, used for partitioning.
           event: Event payload represented as a dictionary.

       Raises:
           TimeoutError: If message delivery does not complete
               within the configured timeout.
           RuntimeError: If Kafka reports a delivery failure.
           ValueError: If the event cannot be JSON-serialized.
        """
        logger.info("About to publish event to Kafka topic %s with key %s", topic, key)
        delivery_errors = []

        def delivery_report(err, msg):
            if err is not None:
                delivery_errors.append(err)

        self._producer.produce(
            topic=topic,
            key=key,
            value=json.dumps(event),
            callback=delivery_report
        )

        remaining = self._producer.flush(timeout=10)

        if remaining > 0:
            raise TimeoutError("Kafka message delivery timed out")

        if delivery_errors:
            raise RuntimeError(
                f"Kafka delivery failed: {delivery_errors[0]}"
            )
