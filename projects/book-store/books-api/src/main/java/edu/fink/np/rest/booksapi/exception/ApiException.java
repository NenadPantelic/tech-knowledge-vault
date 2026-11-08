package edu.fink.np.rest.booksapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ApiException extends RuntimeException {

    private final ErrorReport errorReport;

}
