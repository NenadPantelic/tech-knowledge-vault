package edu.fink.np.rest.booksapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record NewBook(@NotBlank String title,
                      @NotBlank String publisher,
                      @NotNull Integer publishingYear,
                      @Length(min = 0, max = 8192) String description,
                      String edition,
                      Set<Long> authorsIds,
                      Set<Long> genresIds) {
}