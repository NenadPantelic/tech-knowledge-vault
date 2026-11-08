from dataclasses import dataclass

from data.book import Book
from data.enrichment import Enrichment


@dataclass
class EnrichmentRequestEvent:
    event_id: str
    book: Book
    enrichment: Enrichment

    @staticmethod
    def from_dict(payload: dict) -> "EnrichmentRequestEvent":
        event_id = payload.get("event_id")
        book = payload.get("book")
        enrichment = payload.get("enrichment")

        if not event_id or not book or not enrichment:
            raise ValueError("event_id and book and enrichment are required")

        return EnrichmentRequestEvent(event_id, Book(**book), Enrichment(**enrichment))
