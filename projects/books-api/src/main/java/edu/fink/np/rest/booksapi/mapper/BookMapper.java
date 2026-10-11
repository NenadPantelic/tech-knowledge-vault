package edu.fink.np.rest.booksapi.mapper;

import edu.fink.np.rest.booksapi.dto.response.*;
import edu.fink.np.rest.booksapi.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {

    public static BookDTO mapToDTO(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getPublisher(),
                book.getPublishingYear(),
                book.getDescription(),
                book.getEdition().getDisplayValue(),
                AuthorMapper.mapToDTOList(book.getAuthors()),
                GenreMapper.mapToDTOList(book.getGenres())
        );
    }


    public static MinimalBookDTO mapToMinimalDTO(Book book) {
        if (book == null) {
            return null;
        }

        return new MinimalBookDTO(
                book.getId(),
                book.getTitle(),
                book.getPublisher(),
                book.getPublishingYear(),
                book.getEdition().getDisplayValue(),
                AuthorMapper.mapToDTOList(book.getAuthors()),
                GenreMapper.mapToDTOList(book.getGenres())
        );
    }

    public static List<MinimalBookDTO> mapToDTOList(Iterable<Book> books) {
        if (books == null) {
            return null;
        }

        List<MinimalBookDTO> mappedBooks = new ArrayList<>();
        for (Book book : books) {
            mappedBooks.add(BookMapper.mapToMinimalDTO(book));
        }

        return mappedBooks;
    }
}
