package edu.fink.np.rest.booksapi.controller.v2;

import edu.fink.np.rest.booksapi.dto.response.book.BookDTOV2;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV2;
import edu.fink.np.rest.booksapi.service.book.BookServiceV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v2/books")
public class BookControllerV2 {

    private final BookServiceV2 bookServiceV2;

    public BookControllerV2(BookServiceV2 bookServiceV2) {
        this.bookServiceV2 = bookServiceV2;
    }


    @GetMapping
    public List<MinimalBookDTOV2> listBooks(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "50") int size,
                                            @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                            @RequestParam(value = "order", defaultValue = "DESC") String order) {
        log.info("Received a request to list books...");
        return bookServiceV2.listBooks(page, size, sortBy, order);
    }

    @RequestMapping("/{id}")
    @GetMapping
    public BookDTOV2 getBook(@PathVariable("id") UUID bookId) {
        log.info("Received a request to fetch a book by ID: {}", bookId);
        return bookServiceV2.getBook(bookId);
    }
}
