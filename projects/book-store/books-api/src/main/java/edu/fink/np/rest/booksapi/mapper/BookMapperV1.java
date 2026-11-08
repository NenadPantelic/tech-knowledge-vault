package edu.fink.np.rest.booksapi.mapper;

import edu.fink.np.rest.booksapi.dto.response.book.BookDTOV1;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV1;
import edu.fink.np.rest.booksapi.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookMapperV1 {

    public static BookDTOV1 mapToDTO(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDTOV1(
                book.getId(),
                book.getTitle(),
                book.getPublisher(),
                book.getPublishingYear(),
                book.getDescription(),
                book.getPrice(),
                book.getDiscountInPercent(),
                book.getEdition().getDisplayValue(),
                AuthorMapper.mapToDTOList(book.getAuthors()),
                GenreMapper.mapToDTOList(book.getGenres())
        );
    }


    public static MinimalBookDTOV1 mapToMinimalDTO(Book book) {
        if (book == null) {
            return null;
        }

        return new MinimalBookDTOV1(
                book.getId(),
                book.getTitle(),
                book.getPublisher(),
                book.getPublishingYear(),
                book.getEdition().getDisplayValue(),
                book.getPrice(),
                AuthorMapper.mapToDTOList(book.getAuthors()),
                GenreMapper.mapToDTOList(book.getGenres())
        );
    }

    public static List<MinimalBookDTOV1> mapToDTOList(Iterable<Book> books) {
        if (books == null) {
            return null;
        }

        List<MinimalBookDTOV1> mappedBooks = new ArrayList<>();
        for (Book book : books) {
            mappedBooks.add(BookMapperV1.mapToMinimalDTO(book));
        }

        return mappedBooks;
    }
}
