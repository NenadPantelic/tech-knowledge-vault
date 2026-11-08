package edu.fink.np.rest.booksapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@EqualsAndHashCode(exclude = "books") // lazy loaded, no need to use it in hash code
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String fullName;

    private String biography;

    @Builder.Default
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;
}
