from amqp.enrichment_request_consumer import EnrichmentRequestConsumer
from amqp.enrichment_response_producer import EnrichmentResponseProducer
from enrichment.description.random_text_book_description_generator import RandomTextBookDescriptionGenerator
from enrichment.enrichment_executor import EnrichmentExecutor
from logger.logger import configure_logging, get_logger

configure_logging()

logger = get_logger(__name__)

if __name__ == '__main__':
    random_text_book_description_generator = RandomTextBookDescriptionGenerator()
    enrichment_executor = EnrichmentExecutor()
    enrichment_executor.add_enrichment_provider("description", random_text_book_description_generator)
    enrichment_response_producer = EnrichmentResponseProducer()

    enrichment_request_consumer = EnrichmentRequestConsumer(enrichment_executor, enrichment_response_producer)
    enrichment_request_consumer.start()
