package edu.fink.np.rest.booksapi.controller.v1;

import edu.fink.np.rest.booksapi.dto.response.AuthorDTO;
import edu.fink.np.rest.booksapi.dto.response.MinimalAuthorDTO;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV1;
import edu.fink.np.rest.booksapi.service.AuthorService;
import edu.fink.np.rest.booksapi.service.book.BookServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v1/level-three/authors")
// dedicated resources path - better organization and more suitable for caching
// 1. use HTTP verbs
// 2. use status codes
// 3. HATEOAS
public class Level3AuthorController {

    private final AuthorService authorService;
    private final BookServiceV1 bookServiceV1;

    public Level3AuthorController(AuthorService authorService,
                                  BookServiceV1 bookServiceV1) {
        this.authorService = authorService;
        this.bookServiceV1 = bookServiceV1;
    }

    @RequestMapping("/{id}")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDTO getAuthor(@PathVariable("id") Long authorId) {
        log.info("Received a request to fetch an author by ID: {}", authorId);
        return authorService.getAuthor(authorId);
    }


    @GetMapping(produces = {"application/hal+json"})
    public CollectionModel<MinimalAuthorDTO> listAuthors(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "50") int size,
                                                         @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                         @RequestParam(value = "order", defaultValue = "DESC") String order) {
        List<MinimalAuthorDTO> authors = authorService.listAuthors(page, size, sortBy, order);

        for (MinimalAuthorDTO author : authors) {
            Long authorId = author.getId();
            Link selfLink = linkTo(Level3AuthorController.class).slash(authorId).withSelfRel();
            author.add(selfLink);

            // a bit wasteful, we should count here
            List<MinimalBookDTOV1> booksByAuthor = bookServiceV1.findBooksByAuthor(authorId);
            if (booksByAuthor != null && !booksByAuthor.isEmpty()) {
                Link booksLink = linkTo(methodOn(Level3AuthorController.class)
                        .listBooksByAuthor(authorId)).withRel("books");
                author.add(booksLink);
            }
        }

        Link link = linkTo(Level3AuthorController.class).withSelfRel();
        return CollectionModel.of(authors, link);
    }

    @RequestMapping("/{id}/books")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MinimalBookDTOV1> listBooksByAuthor(@PathVariable("id") Long authorId) {
        log.info("Received a request to list books by an author...");
        return bookServiceV1.findBooksByAuthor(authorId);
    }
}
