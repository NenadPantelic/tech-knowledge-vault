package edu.fink.np.rest.booksapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class MinimalAuthorDTO extends RepresentationModel<MinimalAuthorDTO> {

    private final Long id;
    private final String fullName;
}
