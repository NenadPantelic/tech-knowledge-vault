package edu.fink.np.rest.booksapi.service.book.impl;

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


    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImplV1(BookRepository bookRepository,
                             AuthorService authorService,
                             GenreService genreService) {
        super(bookRepository);
        this.authorService = authorService;
        this.genreService = genreService;
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
                .description(newBook.description())
                .price(newBook.price())
                .discountInPercent(newBook.discount())
                .publishingYear(newBook.publishingYear())
                .authors(authors)
                .genres(genres)
                .build();

        book = bookRepository.save(book);
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
        book.setDescription(newBook.description());
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
