package edu.fink.np.rest.booksapi.dto.response.book;

import edu.fink.np.rest.booksapi.dto.response.GenreDTO;
import edu.fink.np.rest.booksapi.dto.response.MinimalAuthorDTO;

import java.util.List;
import java.util.UUID;

public record BookDTOV2(UUID id,
                        String title,
                        String publisher,
                        int publishingYear,
                        String description,
                        float price,
                        float discount,
                        float priceWithDiscount,
                        List<MinimalAuthorDTO> authors,
                        List<GenreDTO> genres) {
}