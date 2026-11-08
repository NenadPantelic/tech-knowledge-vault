package edu.fink.np.rest.booksapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NewGenre(@NotBlank String name) {
}
