import abc

from data.book import Book
from data.enrichment import EnrichmentRequest


class EnrichmentProvider(abc.ABC):
    def __init__(self):
        pass

    def enrich(self, book: Book, enrichment_request: EnrichmentRequest) -> tuple[str, str]:
        """
        Enrich a book.
        :param book: a book to enrich
        :param enrichment_request: an enrichment request that will be used to dictate the enrichment
        :return:
            a tuple of a string enrichment request and an error (nullable).
        """
        pass
