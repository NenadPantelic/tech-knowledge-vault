package edu.fink.np.rest.booksapi.service;

import edu.fink.np.rest.booksapi.dto.request.NewBook;
import edu.fink.np.rest.booksapi.dto.response.BookDTO;
import edu.fink.np.rest.booksapi.dto.response.MinimalBookDTO;
import edu.fink.np.rest.booksapi.model.Book;

import java.util.List;
import java.util.UUID;

public interface BookServiceV1 {

    BookDTO addBook(NewBook newBook);

    List<MinimalBookDTO> listBooks(int page, int size, String sortBy, String sortOrder);

    BookDTO getBook(UUID id);

    BookDTO updateBook(UUID id, NewBook newBook);

    void deleteBook(UUID id);

    List<MinimalBookDTO> findBooksByAuthor(Long authorId);
}
