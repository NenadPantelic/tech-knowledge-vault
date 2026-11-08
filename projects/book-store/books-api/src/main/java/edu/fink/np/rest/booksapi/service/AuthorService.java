package edu.fink.np.rest.booksapi.service;

import edu.fink.np.rest.booksapi.dto.request.NewAuthor;
import edu.fink.np.rest.booksapi.dto.response.AuthorDTO;
import edu.fink.np.rest.booksapi.dto.response.MinimalAuthorDTO;
import edu.fink.np.rest.booksapi.model.Author;

import java.util.List;
import java.util.Set;

public interface AuthorService {

    AuthorDTO addAuthor(NewAuthor newAuthor);

    Author findAuthor(Long id);

    AuthorDTO getAuthor(Long id);

    Set<Author> findAuthors(Set<Long> authorIds);

    List<MinimalAuthorDTO> listAuthors(int page, int size, String sortBy, String sortOrder);
}
