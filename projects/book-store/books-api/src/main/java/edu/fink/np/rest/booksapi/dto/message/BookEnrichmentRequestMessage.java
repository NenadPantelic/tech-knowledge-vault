package edu.fink.np.rest.booksapi.dto.message;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BookEnrichmentRequestMessage(
        @JsonProperty("event_id")
        String eventId,

        BookPayload book,

        EnrichmentOptions enrichment
) {
    public record BookPayload(
            String id,
            String title,
            List<String> authors,

            @JsonProperty("publishing_year")
            Integer publishingYear
    ) {
    }

    public record EnrichmentOptions(
            Description description
    ) {
        public record Description(
                Integer length
        ) {
        }
    }
}