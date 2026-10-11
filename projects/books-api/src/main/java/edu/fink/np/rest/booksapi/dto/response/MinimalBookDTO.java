package edu.fink.np.rest.booksapi.dto.response;

import java.util.List;
import java.util.UUID;

public record MinimalBookDTO(UUID id,
                             String title,
                             String publisher,
                             int publishingYear,
                             String edition,
                             List<MinimalAuthorDTO> authors,
                             List<GenreDTO> genres)  {
}
