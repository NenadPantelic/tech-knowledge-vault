from dataclasses import dataclass


@dataclass
class EnrichmentRequest:
    key: str


@dataclass
class DescriptionGenerationRequest(EnrichmentRequest):
    _KEY = "description"

    length: int = 1

    def __init__(self, length: int):
        super().__init__(DescriptionGenerationRequest._KEY)
        self.length = length


@dataclass
class Enrichment:
    description_generation: DescriptionGenerationRequest = None

    _map: dict = None

    def __init__(self, description_generation_request: DescriptionGenerationRequest):
        self.description_generation = description_generation_request
        self._map = {description_generation_request.key: description_generation_request}

    def map(self):
        return self._map


@dataclass
class EnrichmentResult:
    attribute: str
    value: str
    error: str
