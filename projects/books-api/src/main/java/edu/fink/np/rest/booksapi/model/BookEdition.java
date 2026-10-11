package edu.fink.np.rest.booksapi.model;

import lombok.Getter;

// can be an entity too, to allow the user to freely add new ones and change the existing ones
@Getter
public enum BookEdition {
    FIRST("First Edition"),


    SECOND("Second Edition"),

    SECOND_EXTENDED("Second Extended Edition");


    private final String displayValue;


    BookEdition(String displayValue) {
        this.displayValue = displayValue;
    }


    public static BookEdition fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value cannot be mapped to a valid BookEdition instance");
        }

        for (BookEdition be : BookEdition.values()) {
            if (value.equals(be.displayValue) || value.equals(be.name())) {
                return be;
            }
        }

        throw new IllegalArgumentException(String.format("Unable to map %s to a valid BookEdition instance", value));
    }
}
