package edu.fink.np.rest.booksapi.service.book;

import edu.fink.np.rest.booksapi.dto.response.book.BookDTOV2;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV2;

import java.util.List;
import java.util.UUID;

public interface BookServiceV2 {

    List<MinimalBookDTOV2> listBooks(int page, int size, String sortBy, String sortOrder);

    BookDTOV2 getBook(UUID id);
}
