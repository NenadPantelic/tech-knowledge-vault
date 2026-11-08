from data.enrichment import Enrichment, EnrichmentResult
from enrichment.enrichment_provider import EnrichmentProvider
from logger import logger

ENRICHMENT_FEATURES = {"description"}

logger = logger.get_logger(__name__)


class EnrichmentExecutor:
    def __init__(self):
        self._enrichment_providers = {}

    def add_enrichment_provider(self, feature: str, enrichment_provider: EnrichmentProvider):
        if not feature in ENRICHMENT_FEATURES:
            raise ValueError(f"Unknown enrichment provider {feature}")

        self._enrichment_providers[feature] = enrichment_provider

    def generate_enrichments(self, book, enrichment: Enrichment) -> list[EnrichmentResult]:
        """
        Generate the enrichment results for the given book and enrichment provider.
        :param book: a book to enrich
        :param enrichment: the enrichment request
        :return: a list of enrichment results
        """
        logger.info("Generating an enrichment for book %s", book)
        if enrichment is None:
            return []
        enrichment_results = []

        for feature, enrichment_data in enrichment.map().items():
            provider = self._enrichment_providers.get(feature)
            if not provider:
                logger.warning("No enrichment provider found for feature %s", feature)
                continue

            result, err = provider.enrich(book, enrichment_data)
            enrichment_results.append(EnrichmentResult(feature, result, err))

        return enrichment_results
