package edu.fink.np.rest.booksapi.amqp;

import edu.fink.np.rest.booksapi.dto.message.BookEnrichmentRequestMessage;
import edu.fink.np.rest.booksapi.model.Author;
import edu.fink.np.rest.booksapi.model.Book;
import edu.fink.np.rest.booksapi.properties.EnrichmentProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class BookEnrichmentRequestProducer {

    private static final String TOPIC = "book.enrichment.requested";

    private final KafkaTemplate<String, BookEnrichmentRequestMessage> kafkaTemplate;


    private final BookEnrichmentRequestMessage.EnrichmentOptions enrichmentOptions;

    public BookEnrichmentRequestProducer(EnrichmentProperties enrichmentProperties,
                                         KafkaTemplate<String, BookEnrichmentRequestMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        var descriptionOptions = new BookEnrichmentRequestMessage.EnrichmentOptions.Description(
                enrichmentProperties.description().length()
        );
        enrichmentOptions = new BookEnrichmentRequestMessage.EnrichmentOptions(descriptionOptions);
    }


    public CompletableFuture<SendResult<String, BookEnrichmentRequestMessage>> send(Book book) {
        String eventId = UUID.randomUUID().toString(); // TODO: idempotency...
        BookEnrichmentRequestMessage.BookPayload bookPayload = createBookPayload(book);
        BookEnrichmentRequestMessage bookEnrichmentRequestMessage = new BookEnrichmentRequestMessage(
                eventId, bookPayload, enrichmentOptions
        );

        try {
            return kafkaTemplate.send(TOPIC, book.getId().toString(), bookEnrichmentRequestMessage);
        } catch (Exception e) {
            log.error("Error while sending a BookEnrichmentRequestMessage to Kafka with bookId = {} and " +
                    "eventId = {}. Error: {}", book.getId(), eventId, e.getMessage()
            );
            return null;
        }
    }

    private BookEnrichmentRequestMessage.BookPayload createBookPayload(Book book) {
        String bookId = book.getId().toString();
        List<String> authors = book.getAuthors().stream()
                .map(Author::getFullName)
                .toList();
        return new BookEnrichmentRequestMessage.BookPayload(
                bookId, book.getTitle(), authors, book.getPublishingYear()
        );
    }
}