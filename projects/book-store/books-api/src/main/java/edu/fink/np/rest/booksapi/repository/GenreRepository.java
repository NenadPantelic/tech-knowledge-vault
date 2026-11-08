package edu.fink.np.rest.booksapi.repository;

import edu.fink.np.rest.booksapi.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
}
