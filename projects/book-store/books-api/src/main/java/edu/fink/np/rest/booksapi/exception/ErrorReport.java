package edu.fink.np.rest.booksapi.exception;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record ErrorReport(String message,
                          int statusCode,
                          int internalCode,
                          List<String> errors) {


    public static final ErrorReport BAD_REQUEST = ErrorReport.builder()
            .message("Bad request.")
            .statusCode(400)
            .internalCode(1001)
            .errors(new ArrayList<>())
            .build();

    public static final ErrorReport NOT_FOUND = ErrorReport.builder()
            .message("Not found.")
            .statusCode(404)
            .internalCode(1002)
            .errors(new ArrayList<>())
            .build();


    public static final ErrorReport CONFLICT = ErrorReport.builder()
            .message("Conflict.")
            .statusCode(409)
            .internalCode(1003)
            .errors(new ArrayList<>())
            .build();

    public static final ErrorReport UNPROCESSABLE_ENTITY = ErrorReport.builder()
            .message("Unprocessable entity.")
            .statusCode(422)
            .internalCode(1004)
            .errors(new ArrayList<>())
            .build();

    public static final ErrorReport INTERNAL_SERVER_ERROR = ErrorReport.builder()
            .message("Internal server error.")
            .statusCode(500)
            .internalCode(1101)
            .errors(new ArrayList<>())
            .build();

    public ErrorReport withMessage(String message) {
        return ErrorReport.builder()
                .message(message)
                .statusCode(this.statusCode)
                .internalCode(this.internalCode)
                .errors(this.errors)
                .build();
    }


    public ErrorReport withErrors(List<String> errors) {
        return ErrorReport.builder()
                .message(this.message)
                .statusCode(this.statusCode)
                .internalCode(this.internalCode)
                .errors(errors)
                .build();
    }

    public ErrorReport withMessageAndErrors(String message, List<String> errors) {
        return ErrorReport.builder()
                .message(message)
                .statusCode(this.statusCode)
                .internalCode(this.internalCode)
                .errors(errors)
                .build();
    }

}
