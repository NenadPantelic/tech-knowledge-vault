package edu.fink.np.rest.booksapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String publisher;

    @NotNull
    private Integer publishingYear;

    @Length(min = 0, max = 8192)
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private BookEdition edition = BookEdition.FIRST;

    //  1. Cascade Type PERSIST propagates the persist operation from a parent to a child entity
    //  2. CascadeType.MERGE propagates the merge operation from a parent to a child entity
    //  3. CascadeType.REMOVE propagates the remove operation from parent to child entity. Similar to JPA’s
    //  CascadeType.REMOVE, we have CascadeType.DELETE, which is specific to Hibernate.
    //  4. When we use CascadeType.DETACH, the child entity will also get removed from the persistent context.
    //  5. Unintuitively, CascadeType.LOCK reattaches the entity and its associated child entity with the
    //  persistent context again.
    //  6. When we use this operation with Cascade Type REFRESH, the child entity also gets reloaded from the database
    //  whenever the parent entity is refreshed.
    //  7. CascadeType.REPLICATE The replicate operation is used when we have more than one data source and we want the
    //  data in sync
    //  8. CascadeType.SAVE_UPDATE propagates the same operation to the associated child entity. It’s useful when we use
    //  Hibernate-specific operations like save, update and saveOrUpdate.
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Builder.Default
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public void addAuthor(Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    public void replaceAuthors(Set<Author> authors) {
        // remove references
        Set<Author> removedAuthors = this.authors.stream()
                .filter(a -> !authors.contains(a))
                .collect(Collectors.toSet());

        removedAuthors.forEach(this::removeAuthor);

        // add new references
        authors.forEach(this::addAuthor);
    }


    public void addGenre(Genre genre) {
        genres.add(genre);
        genre.getBooks().add(this);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getBooks().remove(this);
    }

    public void replaceGenres(Set<Genre> genres) {
        // remove references
        Set<Genre> removedGenres = this.genres.stream()
                .filter(g -> !genres.contains(g))
                .collect(Collectors.toSet());

        removedGenres.forEach(this::removeGenre);

        // add new references
        genres.forEach(this::addGenre);
    }
}
