package edu.fink.np.rest.booksapi.service.impl;

import edu.fink.np.rest.booksapi.dto.request.NewGenre;
import edu.fink.np.rest.booksapi.dto.response.GenreDTO;
import edu.fink.np.rest.booksapi.exception.ApiException;
import edu.fink.np.rest.booksapi.exception.ErrorReport;
import edu.fink.np.rest.booksapi.mapper.GenreMapper;
import edu.fink.np.rest.booksapi.model.Genre;
import edu.fink.np.rest.booksapi.repository.GenreRepository;
import edu.fink.np.rest.booksapi.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private static final String SOME_GENRES_DO_NOT_EXIST = "Some genres do not exist!";


    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreDTO addGenre(NewGenre newGenre) {
        log.info("Adding a new genre: {}", newGenre);

        Genre genre = Genre.builder()
                .name(newGenre.name())
                .build();

        genre = genreRepository.save(genre);
        return GenreMapper.mapToDTO(genre);
    }

    @Override
    public Genre findGenre(Long id) {
        log.info("Looking for a genre with id: {}", id);
        return genreRepository.findById(id).orElseThrow(() -> new ApiException(
                ErrorReport.NOT_FOUND.withMessage("Genre not found.")
        ));
    }

    @Override
    public Set<Genre> findGenres(Set<Long> genreIds) {
        log.info("Find genres by their ids: {}", genreIds);
        if (genreIds == null || genreIds.isEmpty()) {
            return Set.of();
        }

        List<Genre> genres = (List<Genre>) genreRepository.findAllById(genreIds);
        if (genres.size() != genreIds.size()) {
            log.error(SOME_GENRES_DO_NOT_EXIST);
            throw new ApiException(ErrorReport.NOT_FOUND
                    .withMessage(SOME_GENRES_DO_NOT_EXIST)
            );
        }

        return new HashSet<>(genres);
    }
}
