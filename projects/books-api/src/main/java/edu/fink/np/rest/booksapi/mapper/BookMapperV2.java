package edu.fink.np.rest.booksapi.mapper;

import edu.fink.np.rest.booksapi.dto.response.book.BookDTOV2;
import edu.fink.np.rest.booksapi.dto.response.book.MinimalBookDTOV2;
import edu.fink.np.rest.booksapi.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookMapperV2 {


    public static BookDTOV2 mapToDTO(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDTOV2(
                book.getId(),
                book.getTitle(),
                book.getPublisher(),
                book.getPublishingYear(),
                book.getDescription(),
                book.getPrice(),
                book.getDiscountInPercent(),
                BookMapperV2.getPriceWithDiscount(book.getPrice(), book.getDiscountInPercent()),
                AuthorMapper.mapToDTOList(book.getAuthors()),
                GenreMapper.mapToDTOList(book.getGenres())
        );
    }


    public static MinimalBookDTOV2 mapToMinimalDTO(Book book) {
        if (book == null) {
            return null;
        }

        return new MinimalBookDTOV2(
                book.getId(),
                book.getTitle(),
                book.getPublisher(),
                book.getPublishingYear(),
                book.getPrice(),
                book.getDiscountInPercent(),
                getPriceWithDiscount(book.getPrice(), book.getDiscountInPercent()),
                AuthorMapper.mapToDTOList(book.getAuthors()),
                GenreMapper.mapToDTOList(book.getGenres())
        );
    }

    public static List<MinimalBookDTOV2> mapToDTOList(Iterable<Book> books) {
        if (books == null) {
            return null;
        }

        List<MinimalBookDTOV2> mappedBooks = new ArrayList<>();
        for (Book book : books) {
            mappedBooks.add(BookMapperV2.mapToMinimalDTO(book));
        }

        return mappedBooks;
    }

    private static float getPriceWithDiscount(float price, float discount) {
        float priceWithDiscount = price / 100 * (100 - discount);
        // for cases where you need better precision, use BigDecimal
        return Math.round(priceWithDiscount * 100.0f) / 100.0f;
    }
}
