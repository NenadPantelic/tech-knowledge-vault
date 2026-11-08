package edu.fink.np.rest.booksapi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "enrichment")
public record EnrichmentProperties(
        Description description
) {
    public record Description(
            int length,
            boolean enabled
    ) {
        public Description {
            if (length <= 0) {
                length = 300;
            }
        }
    }
}