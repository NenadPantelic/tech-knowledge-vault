package edu.fink.np.rest.booksapi.service.book.impl;

import edu.fink.np.rest.booksapi.exception.ApiException;
import edu.fink.np.rest.booksapi.exception.ErrorReport;
import edu.fink.np.rest.booksapi.model.Book;
import edu.fink.np.rest.booksapi.repository.BookRepository;
import edu.fink.np.rest.booksapi.service.book.CommonBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static edu.fink.np.rest.booksapi.common.Constants.ORDER_TO_SORT_DIRECTION_MAP;

@Slf4j
@Service
public class CommonBookServiceImpl implements CommonBookService {

    protected static final String BOOK_NOT_FOUND = "Book not found.";

    protected final BookRepository bookRepository;

    public CommonBookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<Book> findBooks(int page, int size, String sortBy, String sortOrder) {
        log.info("List books using the following paging parameters: page = {}, size = {}, sort by = {}, order = {}",
                page, size, sortBy, sortOrder);

        // TODO: validate sortBy field
        Sort.Direction sortDirection = ORDER_TO_SORT_DIRECTION_MAP.getOrDefault(sortOrder, Sort.Direction.DESC);
        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by(sortDirection, sortBy)
        );

        return bookRepository.findAll(pageRequest);
    }


    @Override
    public Book findById(UUID id) {
        log.info("Get a book by id: {}", id);
        return bookRepository.findById(id).orElseThrow(() -> new ApiException(
                ErrorReport.NOT_FOUND.withMessage(BOOK_NOT_FOUND)
        ));
    }
}
