package edu.fink.np.rest.booksapi.service.book.impl;

import edu.fink.np.rest.booksapi.amqp.BookEnrichmentRequestProducer;
import edu.fink.np.rest.booksapi.dto.message.BookEnrichmentResponseMessage;
import edu.fink.np.rest.booksapi.dto.message.EnrichmentResultStatus;
import edu.fink.np.rest.booksapi.dto.request.NewBook;
import edu.fink.np.rest.booksapi.dto.response.book.BookDTOV1;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV1;
import edu.fink.np.rest.booksapi.exception.ApiException;
import edu.fink.np.rest.booksapi.exception.ErrorReport;
import edu.fink.np.rest.booksapi.mapper.BookMapperV1;
import edu.fink.np.rest.booksapi.model.Author;
import edu.fink.np.rest.booksapi.model.Book;
import edu.fink.np.rest.booksapi.model.BookEdition;
import edu.fink.np.rest.booksapi.model.Genre;
import edu.fink.np.rest.booksapi.repository.BookRepository;
import edu.fink.np.rest.booksapi.service.AuthorService;
import edu.fink.np.rest.booksapi.service.book.BookServiceV1;
import edu.fink.np.rest.booksapi.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class BookServiceImplV1 extends CommonBookServiceImpl implements BookServiceV1 {

    private static final String DESCRIPTION_ATTRIBUTE = "description";

    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookEnrichmentRequestProducer producer;

    public BookServiceImplV1(BookRepository bookRepository,
                             AuthorService authorService,
                             GenreService genreService,
                             BookEnrichmentRequestProducer producer) {
        super(bookRepository);
        this.authorService = authorService;
        this.genreService = genreService;
        this.producer = producer;
    }


    @Transactional
    @Override
    public BookDTOV1 addBook(NewBook newBook) {
        log.info("Adding a new book: {}", newBook);

        Set<Author> authors = authorService.findAuthors(newBook.authorsIds());
        Set<Genre> genres = genreService.findGenres(newBook.genresIds());

        Book book = Book.builder()
                .title(newBook.title())
                .publisher(newBook.publisher())
                .edition(BookEdition.fromValue(newBook.edition()))
                .price(newBook.price())
                .discountInPercent(newBook.discount())
                .publishingYear(newBook.publishingYear())
                .authors(authors)
                .genres(genres)
                .build();

        book = bookRepository.save(book);
        String bookId = book.getId().toString();

        producer.send(book)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Unable to enrich the book[id = {}]", bookId);
                    } else {
                        log.info("Successfully sent the book[id = {}] to enrichment...", bookId);
                    }
                });


        return BookMapperV1.mapToDTO(book);
    }


    @Override
    public List<MinimalBookDTOV1> listBooks(int page, int size, String sortBy, String sortOrder) {
        Page<Book> books = findBooks(page, size, sortBy, sortOrder);
        return BookMapperV1.mapToDTOList(books);
    }

    @Override
    public BookDTOV1 getBook(UUID id) {
        Book book = findById(id);
        return BookMapperV1.mapToDTO(book);
    }

    @Transactional
    @Override
    public BookDTOV1 updateBook(UUID id, NewBook newBook) {
        log.info("Updating a book[id = {}] with new data {}", id, newBook);
        Book book = findById(id);

        Set<Author> authors = authorService.findAuthors(newBook.authorsIds());
        Set<Genre> genres = genreService.findGenres(newBook.genresIds());

        book.setTitle(newBook.title());
        book.setEdition(BookEdition.fromValue(newBook.edition()));
        book.setPrice(newBook.price());
        book.setDiscountInPercent(newBook.discount());
        book.setPublisher(newBook.publisher());
        book.setPublishingYear(newBook.publishingYear());

        book.replaceAuthors(authors);
        book.replaceGenres(genres);

        book = bookRepository.save(book);
        return BookMapperV1.mapToDTO(book);
    }

    @Override
    public void enrichBook(UUID bookId, List<BookEnrichmentResponseMessage.EnrichmentResult> enrichmentResults) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            log.warn("Unable to enrich book[id = {}]. Book not found!", bookId);
            return;
        }

        Book book = bookOptional.get();

        for (var result : enrichmentResults) {
            if (result.status() == EnrichmentResultStatus.FAILED) {
                log.error("Book enrichment has faced an error: {}. Attribute {} will not be enriched.", result.error(), result.attribute());
                continue;
            }

            if (DESCRIPTION_ATTRIBUTE.equals(result.attribute())) {
                book.setDescription(result.value());
            } else {
                log.info("The attribute is unknown. Skipping the enrichment...");
            }
        }

        bookRepository.save(book);
    }

    @Override
    public void deleteBook(UUID id) {
        log.info("Delete a book by id: {}", id);
        int count = bookRepository.deleteWithCountById(id);

        if (count == 0) {
            log.error("Book with id {} does not exist.", id);
            throw new ApiException(
                    ErrorReport.NOT_FOUND.withMessage(BOOK_NOT_FOUND)
            );
        }
    }

    @Override
    public List<MinimalBookDTOV1> findBooksByAuthor(Long authorId) {
        log.info("Find all books of the author with id: {}", authorId);
        Author author = authorService.findAuthor(authorId);
        return BookMapperV1.mapToDTOList(author.getBooks());
    }
}
