package edu.fink.np.rest.booksapi.controller.v1;

import edu.fink.np.rest.booksapi.dto.request.NewBook;
import edu.fink.np.rest.booksapi.dto.response.BookDTO;
import edu.fink.np.rest.booksapi.service.BookServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/level-zero/books")// dedicated resources path - better organization and more suitable for caching
// both XML and JSON are fine
// should always return 200, this one return error status codes by RFC though
public class Level1BookController {

    private final BookServiceV1 bookServiceV1;

    public Level1BookController(BookServiceV1 bookServiceV1) {
        this.bookServiceV1 = bookServiceV1;
    }


    @PostMapping
    public BookDTO addBook(@RequestBody NewBook newBook) {
        log.info("Received a request to add a new book");
        return bookServiceV1.addBook(newBook);
    }

    @RequestMapping("/{id}")
    @PostMapping
    public BookDTO getBook(@PathVariable("id") UUID bookId) {
        log.info("Received a request to fetch a book by ID: {}", bookId);
        return bookServiceV1.getBook(bookId);
    }
}
