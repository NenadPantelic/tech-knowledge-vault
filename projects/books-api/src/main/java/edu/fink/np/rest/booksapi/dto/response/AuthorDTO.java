package edu.fink.np.rest.booksapi.dto.response;

public record AuthorDTO(Long id,
                        String fullName,
                        String biography) {
}
