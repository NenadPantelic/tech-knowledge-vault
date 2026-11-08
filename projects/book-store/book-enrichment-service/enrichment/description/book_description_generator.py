import abc
from logger import logger

from data.book import Book
from data.enrichment import EnrichmentResult, EnrichmentRequest
from enrichment.enrichment_provider import EnrichmentProvider

logger = logger.get_logger(__name__)


class BookDescriptionGenerator(EnrichmentProvider):

    def __init__(self):
        super().__init__()

    def enrich(self, book: Book, enrichment_request: EnrichmentRequest) -> tuple[str, None] | tuple[None, str]:
        try:
            logger.info(f"Generating description for {book.title}")
            return self.generate(book, enrichment_request), None
        except Exception as ex:
            logger.error("Book enrichment failed when generating a description: %s", ex)
            return None, str(ex)

    @abc.abstractmethod
    def generate(self, book: Book, enrichment_request: EnrichmentRequest) -> str:
        """
        Generates a description of a book.

        :param book: a book to generate the description for
        :param enrichment_request: enrichment request to instruct the enrichment provider
        :return:
        """
        pass
