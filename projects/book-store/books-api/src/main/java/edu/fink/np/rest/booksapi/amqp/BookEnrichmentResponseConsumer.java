package edu.fink.np.rest.booksapi.amqp;

import edu.fink.np.rest.booksapi.dto.message.BookEnrichmentResponseMessage;
import edu.fink.np.rest.booksapi.service.book.BookServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookEnrichmentResponseConsumer {

    private final BookServiceV1 bookService;

    public BookEnrichmentResponseConsumer(BookServiceV1 bookService) {
        this.bookService = bookService;
    }

    @KafkaListener(
            topics = "book.enrichment.completed",
            groupId = "book-service"
    )
    public void consume(BookEnrichmentResponseMessage event) {
        log.info(
                "Received enrichment response: eventId={}, correlationId={}, bookId={}, status={}",
                event.eventId(),
                event.correlationId(),
                event.bookId(),
                event.status()
        );

        switch (event.status()) {
            case SUCCESS, PARTIAL_SUCCESS -> bookService.enrichBook(event.bookId(), event.results());

            case FAILED -> log.error(
                    "Enrichment failed for book {}",
                    event.bookId()
            );
        }
    }
}