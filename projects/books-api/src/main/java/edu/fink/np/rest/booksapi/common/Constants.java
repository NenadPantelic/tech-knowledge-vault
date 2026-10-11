package edu.fink.np.rest.booksapi.common;

import org.springframework.data.domain.Sort;

import java.util.Map;

public final class Constants {


    private Constants() {

    }

    public static final Map<String, Sort.Direction> ORDER_TO_SORT_DIRECTION_MAP = Map.of(
            "ASC", Sort.Direction.ASC,
            "DESC", Sort.Direction.DESC
    );

}
