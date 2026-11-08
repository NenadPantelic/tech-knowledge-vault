package edu.fink.np.rest.booksapi.service.book;

import edu.fink.np.rest.booksapi.model.Book;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CommonBookService {

    Page<Book> findBooks(int page, int size, String sortBy, String sortOrder);

    Book findById(UUID id);
}
