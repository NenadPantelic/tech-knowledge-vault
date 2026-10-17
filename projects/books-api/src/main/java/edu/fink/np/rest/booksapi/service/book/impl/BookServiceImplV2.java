package edu.fink.np.rest.booksapi.service.book.impl;

import edu.fink.np.rest.booksapi.dto.response.book.BookDTOV2;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV2;
import edu.fink.np.rest.booksapi.mapper.BookMapperV2;
import edu.fink.np.rest.booksapi.model.Book;
import edu.fink.np.rest.booksapi.repository.BookRepository;
import edu.fink.np.rest.booksapi.service.book.BookServiceV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BookServiceImplV2 extends CommonBookServiceImpl implements BookServiceV2 {

    public BookServiceImplV2(BookRepository bookRepository) {
        super(bookRepository);
    }

    @Override
    public List<MinimalBookDTOV2> listBooks(int page, int size, String sortBy, String sortOrder) {
        Page<Book> books = findBooks(page, size, sortBy, sortOrder);
        return BookMapperV2.mapToDTOList(books);
    }

    @Override
    public BookDTOV2 getBook(UUID id) {
        Book book = findById(id);
        return BookMapperV2.mapToDTO(book);
    }
}

