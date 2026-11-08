package edu.fink.np.rest.booksapi.dto.message;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record BookEnrichmentResponseMessage(

        @JsonProperty("event_id")
        String eventId,

        @JsonProperty("correlation_id")
        String correlationId,

        @JsonProperty("event_type")
        String eventType,

        @JsonProperty("book_id")
        UUID bookId,

        EnrichmentStatus status,

        List<EnrichmentResult> results
) {
    public record EnrichmentResult(
            String attribute,
            EnrichmentResultStatus status,
            String value,
            EnrichmentError error
    ) {

    }

    public record EnrichmentError(
            String code,
            String message
    ) {
    }
}