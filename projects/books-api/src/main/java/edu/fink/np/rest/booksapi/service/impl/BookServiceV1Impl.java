package edu.fink.np.rest.booksapi.service.impl;

import edu.fink.np.rest.booksapi.dto.request.NewBook;
import edu.fink.np.rest.booksapi.dto.response.BookDTO;
import edu.fink.np.rest.booksapi.dto.response.MinimalBookDTO;
import edu.fink.np.rest.booksapi.exception.ApiException;
import edu.fink.np.rest.booksapi.exception.ErrorReport;
import edu.fink.np.rest.booksapi.mapper.BookMapper;
import edu.fink.np.rest.booksapi.model.Author;
import edu.fink.np.rest.booksapi.model.Book;
import edu.fink.np.rest.booksapi.model.BookEdition;
import edu.fink.np.rest.booksapi.model.Genre;
import edu.fink.np.rest.booksapi.repository.BookRepository;
import edu.fink.np.rest.booksapi.service.AuthorService;
import edu.fink.np.rest.booksapi.service.BookServiceV1;
import edu.fink.np.rest.booksapi.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static edu.fink.np.rest.booksapi.common.Constants.ORDER_TO_SORT_DIRECTION_MAP;

@Slf4j
@Service
public class BookServiceV1Impl implements BookServiceV1 {

    private static final String BOOK_NOT_FOUND = "Book not found.";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceV1Impl(BookRepository bookRepository,
                             AuthorService authorService,
                             GenreService genreService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }


    @Transactional
    @Override
    public BookDTO addBook(NewBook newBook) {
        log.info("Adding a new book: {}", newBook);

        Set<Author> authors = authorService.findAuthors(newBook.authorsIds());
        Set<Genre> genres = genreService.findGenres(newBook.genresIds());

        Book book = Book.builder()
                .title(newBook.title())
                .publisher(newBook.publisher())
                .edition(BookEdition.fromValue(newBook.edition()))
                .description(newBook.description())
                .publishingYear(newBook.publishingYear())
                .authors(authors)
                .genres(genres)
                .build();

        book = bookRepository.save(book);
        return BookMapper.mapToDTO(book);
    }

    @Override
    public List<MinimalBookDTO> listBooks(int page, int size, String sortBy, String sortOrder) {
        log.info("List books using the following paging parameters: page = {}, size = {}, sort by = {}, order = {}",
                page, size, sortBy, sortOrder);

        // TODO: validate sortBy field
        Sort.Direction sortDirection = ORDER_TO_SORT_DIRECTION_MAP.getOrDefault(sortOrder, Sort.Direction.DESC);
        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(sortDirection, sortBy)
        );

        Page<Book> books = bookRepository.findAll(pageRequest);
        return BookMapper.mapToDTOList(books);
    }

    @Override
    public BookDTO getBook(UUID id) {
        Book book = findById(id);
        return BookMapper.mapToDTO(book);
    }

    @Transactional
    @Override
    public BookDTO updateBook(UUID id, NewBook newBook) {
        log.info("Updating a book[id = {}] with new data {}", id, newBook);
        Book book = findById(id);

        Set<Author> authors = authorService.findAuthors(newBook.authorsIds());
        Set<Genre> genres = genreService.findGenres(newBook.genresIds());

        book.setTitle(newBook.title());
        book.setEdition(BookEdition.fromValue(newBook.edition()));
        book.setDescription(newBook.description());
        book.setPublisher(newBook.publisher());
        book.setPublishingYear(newBook.publishingYear());

        book.replaceAuthors(authors);
        book.replaceGenres(genres);

        book = bookRepository.save(book);
        return BookMapper.mapToDTO(book);
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
    public List<MinimalBookDTO> findBooksByAuthor(Long authorId) {
        log.info("Find all books of the author with id: {}", authorId);
        Author author = authorService.findAuthor(authorId);
        return BookMapper.mapToDTOList(author.getBooks());
    }

    private Book findById(UUID id) {
        log.info("Get a book by id: {}", id);
        return bookRepository.findById(id).orElseThrow(() -> new ApiException(
                ErrorReport.NOT_FOUND.withMessage(BOOK_NOT_FOUND)
        ));
    }
}
