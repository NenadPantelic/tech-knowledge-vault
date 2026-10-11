package edu.fink.np.rest.booksapi.controller.v1;

import edu.fink.np.rest.booksapi.dto.request.NewBook;
import edu.fink.np.rest.booksapi.dto.response.BookDTO;
import edu.fink.np.rest.booksapi.service.BookServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/level-two/books")
// dedicated resources path - better organization and more suitable for caching
// 1. use HTTP verbs
// 2. use status codes
public class Level2BookController {

    private final BookServiceV1 bookServiceV1;

    public Level2BookController(BookServiceV1 bookServiceV1) {
        this.bookServiceV1 = bookServiceV1;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public BookDTO addBook(@RequestBody NewBook newBook) {
        log.info("Received a request to add a new book");
        return bookServiceV1.addBook(newBook);
    }

    @RequestMapping("/{id}")
    @GetMapping
    public BookDTO getBook(@PathVariable("id") UUID bookId) {
        log.info("Received a request to fetch a book by ID: {}", bookId);
        return bookServiceV1.getBook(bookId);
    }
}
