package edu.fink.np.rest.booksapi.mapper;

import edu.fink.np.rest.booksapi.dto.response.GenreDTO;
import edu.fink.np.rest.booksapi.model.Genre;

import java.util.Collection;
import java.util.List;

public class GenreMapper {

    public static GenreDTO mapToDTO(Genre genre) {
        if (genre == null) {
            return null;
        }

        return new GenreDTO(
                genre.getId(),
                genre.getName()
        );
    }


    public static List<GenreDTO> mapToDTOList(Collection<Genre> genres) {
        if (genres == null) {
            return null;
        }


        return genres.stream()
                .map(GenreMapper::mapToDTO)
                .toList();
    }
}
