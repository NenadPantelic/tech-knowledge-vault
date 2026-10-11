package edu.fink.np.rest.booksapi.service;

import edu.fink.np.rest.booksapi.dto.request.NewGenre;
import edu.fink.np.rest.booksapi.dto.response.GenreDTO;
import edu.fink.np.rest.booksapi.model.Genre;

import java.util.Set;

public interface GenreService {

    GenreDTO addGenre(NewGenre newGenre);

    Genre findGenre(Long id);

    Set<Genre> findGenres(Set<Long> genreIds);
}
