package edu.fink.np.rest.booksapi.repository;

import edu.fink.np.rest.booksapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    @Modifying
    @Transactional // any custom or modifying delete execution requires an active transaction context
    @Query("DELETE FROM Book b WHERE b.id = :id")
    int deleteWithCountById(UUID id);
}
