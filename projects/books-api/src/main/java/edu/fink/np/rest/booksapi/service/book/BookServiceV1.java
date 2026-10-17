package edu.fink.np.rest.booksapi.service.book;

import edu.fink.np.rest.booksapi.dto.request.NewBook;
import edu.fink.np.rest.booksapi.dto.response.book.BookDTOV1;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV1;

import java.util.List;
import java.util.UUID;

public interface BookServiceV1 {

    BookDTOV1 addBook(NewBook newBook);

    List<MinimalBookDTOV1> listBooks(int page, int size, String sortBy, String sortOrder);

    BookDTOV1 getBook(UUID id);

    BookDTOV1 updateBook(UUID id, NewBook newBook);

    void deleteBook(UUID id);

    List<MinimalBookDTOV1> findBooksByAuthor(Long authorId);
}
