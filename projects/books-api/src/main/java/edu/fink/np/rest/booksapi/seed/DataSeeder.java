package edu.fink.np.rest.booksapi.seed;

import edu.fink.np.rest.booksapi.dto.request.NewAuthor;
import edu.fink.np.rest.booksapi.dto.request.NewBook;
import edu.fink.np.rest.booksapi.dto.request.NewGenre;
import edu.fink.np.rest.booksapi.dto.response.AuthorDTO;
import edu.fink.np.rest.booksapi.dto.response.BookDTO;
import edu.fink.np.rest.booksapi.dto.response.GenreDTO;
import edu.fink.np.rest.booksapi.service.AuthorService;
import edu.fink.np.rest.booksapi.service.BookServiceV1;
import edu.fink.np.rest.booksapi.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataSeeder {

    private static final List<String> GENRES = List.of(
            "Fantasy",
            "Science Fiction",
            "Historical Fiction",
            "Mystery",
            "Thriller",
            "Romance",
            "Horror",
            "Literary Fiction",
            "Adventure",
            "Dystopian"
    );


    private static final List<NewAuthor> AUTHORS = List.of(
            new NewAuthor(
                    "J.R.R. Tolkien",
                    "English writer, philologist, and academic best known as the author of The Hobbit and The Lord of the Rings."
            ),
            new NewAuthor(
                    "J.K. Rowling",
                    "British author best known for the Harry Potter fantasy series, one of the world's most successful book franchises."
            ),
            new NewAuthor(
                    "George R.R. Martin",
                    "American novelist and short story writer best known for the epic fantasy series A Song of Ice and Fire."
            ),
            new NewAuthor(
                    "C.S. Lewis",
                    "British writer and scholar best known for The Chronicles of Narnia and his works of Christian literature."
            ),
            new NewAuthor(
                    "Frank Herbert",
                    "American science fiction author best known for Dune, a landmark novel about politics, ecology, and power."
            ),
            new NewAuthor(
                    "Isaac Asimov",
                    "American writer and professor best known for his influential science fiction novels and short stories."
            ),
            new NewAuthor(
                    "Ursula K. Le Guin",
                    "American author celebrated for influential science fiction and fantasy exploring society, culture, and identity."
            ),
            new NewAuthor(
                    "Victor Hugo",
                    "French Romantic writer best known for Les Misérables and The Hunchback of Notre-Dame."
            ),
            new NewAuthor(
                    "Leo Tolstoy",
                    "Russian novelist regarded as one of the greatest writers in world literature, best known for War and Peace."
            ),
            new NewAuthor(
                    "Markus Zusak",
                    "Australian author best known for The Book Thief, a novel set in Nazi Germany and narrated by Death."
            ),
            new NewAuthor(
                    "Agatha Christie",
                    "English writer renowned for her detective novels featuring Hercule Poirot and Miss Marple."
            ),
            new NewAuthor(
                    "Arthur Conan Doyle",
                    "Scottish writer and physician best known for creating the detective Sherlock Holmes."
            ),
            new NewAuthor(
                    "Stieg Larsson",
                    "Swedish journalist and author best known for the Millennium series, beginning with The Girl with the Dragon Tattoo."
            ),
            new NewAuthor(
                    "Gillian Flynn",
                    "American author known for psychological thrillers including Gone Girl, Sharp Objects, and Dark Places."
            ),
            new NewAuthor(
                    "Thomas Harris",
                    "American author best known for psychological thrillers featuring the fictional serial killer Hannibal Lecter."
            ),
            new NewAuthor(
                    "Jane Austen",
                    "English novelist known for her sharp social observations and novels including Pride and Prejudice."
            ),
            new NewAuthor(
                    "Charlotte Brontë",
                    "English novelist best known for Jane Eyre, a landmark work of Victorian literature."
            ),
            new NewAuthor(
                    "Emily Brontë",
                    "English novelist and poet best known for Wuthering Heights, a classic of English literature."
            ),
            new NewAuthor(
                    "Bram Stoker",
                    "Irish author best known for Dracula, one of the defining novels of Gothic horror."
            ),
            new NewAuthor(
                    "Mary Shelley",
                    "English novelist best known for Frankenstein, a foundational work of science fiction and Gothic literature."
            ),
            new NewAuthor(
                    "Stephen King",
                    "American author renowned for his prolific body of horror, supernatural fiction, and thriller novels."
            ),
            new NewAuthor(
                    "F. Scott Fitzgerald",
                    "American novelist of the Jazz Age best known for The Great Gatsby."
            ),
            new NewAuthor(
                    "Harper Lee",
                    "American novelist best known for To Kill a Mockingbird, a classic of American literature."
            ),
            new NewAuthor(
                    "Gabriel García Márquez",
                    "Colombian novelist and Nobel laureate best known for One Hundred Years of Solitude."
            ),
            new NewAuthor(
                    "Alexandre Dumas",
                    "French novelist best known for adventure classics including The Count of Monte Cristo and The Three Musketeers."
            ),
            new NewAuthor(
                    "Robert Louis Stevenson",
                    "Scottish novelist and poet best known for adventure classics including Treasure Island."
            ),
            new NewAuthor(
                    "Jules Verne",
                    "French novelist and pioneer of science fiction best known for adventure novels including Around the World in Eighty Days."
            ),
            new NewAuthor(
                    "George Orwell",
                    "English novelist and essayist best known for Nineteen Eighty-Four and Animal Farm."
            ),
            new NewAuthor(
                    "Aldous Huxley",
                    "English writer and philosopher best known for the dystopian novel Brave New World."
            ),
            new NewAuthor(
                    "Ray Bradbury",
                    "American author best known for Fahrenheit 451 and his influential science fiction and fantasy works."
            )
    );


    private static final List<NewBook> BOOKS = List.of(
            new NewBook(
                    "The Lord of the Rings",
                    "Allen & Unwin",
                    1954,
                    "An epic fantasy journey following Frodo Baggins and his companions as they seek to destroy the One Ring.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Hobbit",
                    "Allen & Unwin",
                    1937,
                    "Bilbo Baggins joins a dangerous quest to reclaim the Lonely Mountain and its treasure from a dragon.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Harry Potter and the Philosopher's Stone",
                    "Bloomsbury",
                    1997,
                    "A young wizard discovers his magical heritage and begins his education at Hogwarts School of Witchcraft and Wizardry.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "A Game of Thrones",
                    "Bantam Books",
                    1996,
                    "Noble families struggle for power while an ancient threat awakens beyond the northern frontier.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Chronicles of Narnia",
                    "Geoffrey Bles",
                    1950,
                    "Four children enter a magical world where they become involved in an epic struggle between good and evil.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Dune",
                    "Chilton Books",
                    1965,
                    "A young nobleman becomes embroiled in politics, warfare, and prophecy on the desert planet Arrakis.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Foundation",
                    "Gnome Press",
                    1951,
                    "A mathematician develops a science of predicting humanity's future and attempts to preserve civilization.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Left Hand of Darkness",
                    "Ace Books",
                    1969,
                    "An envoy visits an alien world whose inhabitants challenge conventional ideas about gender and society.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Les Misérables",
                    "A. Lacroix, Verboeckhoven & Cie",
                    1862,
                    "An epic tale of justice, redemption, love, and revolution in nineteenth-century France.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "War and Peace",
                    "The Russian Messenger",
                    1869,
                    "An epic portrait of Russian society during the Napoleonic Wars, following families through war and upheaval.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Book Thief",
                    "Picador",
                    2005,
                    "A young girl finds solace in books while growing up in Nazi Germany during World War II.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Murder of Roger Ackroyd",
                    "William Collins, Sons",
                    1926,
                    "Hercule Poirot investigates a mysterious murder in an English village where everyone has something to hide.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "And Then There Were None",
                    "Collins Crime Club",
                    1939,
                    "Ten strangers are invited to an isolated island where they are mysteriously killed one by one.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Hound of the Baskervilles",
                    "George Newnes",
                    1902,
                    "Sherlock Holmes investigates a legendary supernatural hound said to haunt the Baskerville family.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Girl with the Dragon Tattoo",
                    "Norstedts",
                    2005,
                    "A journalist and a brilliant hacker investigate the decades-old disappearance of a wealthy man's niece.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Gone Girl",
                    "Crown Publishing Group",
                    2012,
                    "A man's wife disappears on their anniversary, turning their troubled marriage into a national mystery.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Silence of the Lambs",
                    "St. Martin's Press",
                    1988,
                    "An FBI trainee seeks the help of an imprisoned killer to catch another dangerous murderer.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Pride and Prejudice",
                    "T. Egerton",
                    1813,
                    "Elizabeth Bennet navigates love, family expectations, and social class in nineteenth-century England.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Jane Eyre",
                    "Smith, Elder & Co.",
                    1847,
                    "An orphaned young woman struggles for independence and finds love while working as a governess.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Wuthering Heights",
                    "Thomas Cautley Newby",
                    1847,
                    "A passionate and destructive love story unfolds against the bleak landscape of the Yorkshire moors.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Dracula",
                    "Archibald Constable and Company",
                    1897,
                    "An English solicitor travels to Transylvania and encounters the mysterious Count Dracula.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Frankenstein",
                    "Lackington, Hughes, Harding, Mavor & Jones",
                    1818,
                    "A scientist creates a living being whose existence leads to tragedy, isolation, and revenge.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Shining",
                    "Doubleday",
                    1977,
                    "A family isolated in a remote hotel encounters terrifying supernatural forces during the winter.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Great Gatsby",
                    "Charles Scribner's Sons",
                    1925,
                    "A mysterious millionaire's pursuit of a lost love exposes the illusions of wealth and the American Dream.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "To Kill a Mockingbird",
                    "J. B. Lippincott & Co.",
                    1960,
                    "A young girl observes injustice and racial prejudice in a small Alabama town during the 1930s.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "One Hundred Years of Solitude",
                    "Editorial Sudamericana",
                    1967,
                    "Several generations of the Buendía family experience love, war, tragedy, and magical events in a fictional town.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "The Count of Monte Cristo",
                    "Pétion",
                    1844,
                    "A man falsely imprisoned for years escapes and uses a hidden fortune to seek revenge on those who betrayed him.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Treasure Island",
                    "Cassell & Co.",
                    1883,
                    "A young boy joins a dangerous voyage in search of buried pirate treasure.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Around the World in Eighty Days",
                    "Jules Hetzel",
                    1872,
                    "Phileas Fogg attempts to circumnavigate the globe in eighty days after making an extraordinary wager.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "1984",
                    "Secker & Warburg",
                    1949,
                    "In a totalitarian future, Winston Smith secretly rebels against a regime that controls truth and human thought.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Brave New World",
                    "Chatto & Windus",
                    1932,
                    "A futuristic society maintains stability through genetic engineering, conditioning, and artificial happiness.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            ),
            new NewBook(
                    "Fahrenheit 451",
                    "Ballantine Books",
                    1953,
                    "In a future society where books are forbidden, a fireman begins questioning the system he serves.",
                    "First Edition",
                    Set.of(),
                    Set.of()
            )
    );

    private static final Map<String, String> BOOK_TO_AUTHOR = Map.ofEntries(
            Map.entry("The Lord of the Rings", "J.R.R. Tolkien"),
            Map.entry("The Hobbit", "J.R.R. Tolkien"),
            Map.entry("Harry Potter and the Philosopher's Stone", "J.K. Rowling"),
            Map.entry("A Game of Thrones", "George R.R. Martin"),
            Map.entry("The Chronicles of Narnia", "C.S. Lewis"),
            Map.entry("Dune", "Frank Herbert"),
            Map.entry("Foundation", "Isaac Asimov"),
            Map.entry("The Left Hand of Darkness", "Ursula K. Le Guin"),
            Map.entry("Les Misérables", "Victor Hugo"),
            Map.entry("War and Peace", "Leo Tolstoy"),
            Map.entry("The Book Thief", "Markus Zusak"),
            Map.entry("The Murder of Roger Ackroyd", "Agatha Christie"),
            Map.entry("And Then There Were None", "Agatha Christie"),
            Map.entry("The Hound of the Baskervilles", "Arthur Conan Doyle"),
            Map.entry("The Girl with the Dragon Tattoo", "Stieg Larsson"),
            Map.entry("Gone Girl", "Gillian Flynn"),
            Map.entry("The Silence of the Lambs", "Thomas Harris"),
            Map.entry("Pride and Prejudice", "Jane Austen"),
            Map.entry("Jane Eyre", "Charlotte Brontë"),
            Map.entry("Wuthering Heights", "Emily Brontë"),
            Map.entry("Dracula", "Bram Stoker"),
            Map.entry("Frankenstein", "Mary Shelley"),
            Map.entry("The Shining", "Stephen King"),
            Map.entry("The Great Gatsby", "F. Scott Fitzgerald"),
            Map.entry("To Kill a Mockingbird", "Harper Lee"),
            Map.entry("One Hundred Years of Solitude", "Gabriel García Márquez"),
            Map.entry("The Count of Monte Cristo", "Alexandre Dumas"),
            Map.entry("Treasure Island", "Robert Louis Stevenson"),
            Map.entry("Around the World in Eighty Days", "Jules Verne"),
            Map.entry("1984", "George Orwell"),
            Map.entry("Brave New World", "Aldous Huxley"),
            Map.entry("Fahrenheit 451", "Ray Bradbury")
    );

    private static final Map<String, List<String>> BOOK_TO_GENRES = Map.ofEntries(
            Map.entry("The Lord of the Rings",
                    List.of("Fantasy", "Adventure", "Literary Fiction")),

            Map.entry("The Hobbit",
                    List.of("Fantasy", "Adventure")),

            Map.entry("Harry Potter and the Philosopher's Stone",
                    List.of("Fantasy", "Adventure")),

            Map.entry("A Game of Thrones",
                    List.of("Fantasy", "Adventure")),

            Map.entry("The Chronicles of Narnia",
                    List.of("Fantasy", "Adventure")),

            Map.entry("Dune",
                    List.of("Science Fiction", "Adventure", "Literary Fiction")),

            Map.entry("Foundation",
                    List.of("Science Fiction")),

            Map.entry("The Left Hand of Darkness",
                    List.of("Science Fiction", "Literary Fiction")),

            Map.entry("Les Misérables",
                    List.of("Historical Fiction", "Literary Fiction", "Adventure")),

            Map.entry("War and Peace",
                    List.of("Historical Fiction", "Literary Fiction", "Romance")),

            Map.entry("The Book Thief",
                    List.of("Historical Fiction", "Literary Fiction")),

            Map.entry("The Murder of Roger Ackroyd",
                    List.of("Mystery")),

            Map.entry("And Then There Were None",
                    List.of("Mystery", "Thriller")),

            Map.entry("The Hound of the Baskervilles",
                    List.of("Mystery", "Horror", "Adventure")),

            Map.entry("The Girl with the Dragon Tattoo",
                    List.of("Mystery", "Thriller")),

            Map.entry("Gone Girl",
                    List.of("Mystery", "Thriller")),

            Map.entry("The Silence of the Lambs",
                    List.of("Thriller", "Mystery", "Horror")),

            Map.entry("Pride and Prejudice",
                    List.of("Romance", "Literary Fiction")),

            Map.entry("Jane Eyre",
                    List.of("Romance", "Horror", "Literary Fiction")),

            Map.entry("Wuthering Heights",
                    List.of("Romance", "Horror", "Literary Fiction")),

            Map.entry("Dracula",
                    List.of("Horror", "Literary Fiction")),

            Map.entry("Frankenstein",
                    List.of("Horror", "Science Fiction", "Literary Fiction")),

            Map.entry("The Shining",
                    List.of("Horror", "Thriller")),

            Map.entry("The Great Gatsby",
                    List.of("Literary Fiction", "Romance")),

            Map.entry("To Kill a Mockingbird",
                    List.of("Literary Fiction", "Historical Fiction")),

            Map.entry("One Hundred Years of Solitude",
                    List.of("Literary Fiction", "Fantasy")),

            Map.entry("The Count of Monte Cristo",
                    List.of("Adventure", "Historical Fiction", "Thriller")),

            Map.entry("Treasure Island",
                    List.of("Adventure")),

            Map.entry("Around the World in Eighty Days",
                    List.of("Adventure", "Science Fiction")),

            Map.entry("1984",
                    List.of("Dystopian", "Science Fiction", "Literary Fiction")),

            Map.entry("Brave New World",
                    List.of("Dystopian", "Science Fiction", "Literary Fiction")),

            Map.entry("Fahrenheit 451",
                    List.of("Dystopian", "Science Fiction", "Literary Fiction"))
    );

    private final AuthorService authorService;
    private final BookServiceV1 bookServiceV1;
    private final GenreService genreService;


    public DataSeeder(AuthorService authorService, BookServiceV1 bookServiceV1, GenreService genreService) {
        this.authorService = authorService;
        this.bookServiceV1 = bookServiceV1;
        this.genreService = genreService;
    }


    public void seed() {
        log.info("Seeding data...");
        Map<String, GenreDTO> genreIdToGenreMap = createGenres();
        Map<String, AuthorDTO> authorIdToAuthorMap = createAuthors();

        BOOKS.forEach(book -> {
            Set<Long> authorsIds = findBookAuthors(book.title(), authorIdToAuthorMap);
            Set<Long> genresIds = findBookGenres(book.title(), genreIdToGenreMap);

            // records are immutable
            NewBook newBook = new NewBook(
                    book.title(),
                    book.publisher(),
                    book.publishingYear(),
                    book.description(),
                    book.edition(),
                    authorsIds, genresIds
            );

            BookDTO createdBook = bookServiceV1.addBook(newBook);
            log.info("A new book has been created: {}", createdBook);
        });

    }

    private Map<String, GenreDTO> createGenres() {
        return GENRES.stream()
                .map(g -> {
                    GenreDTO createdGenre = genreService.addGenre(new NewGenre(g));
                    log.info("A new genre has been created: {}", createdGenre);
                    return createdGenre;
                }).collect(Collectors.toMap(GenreDTO::name, g -> g));
    }

    private Map<String, AuthorDTO> createAuthors() {
        return AUTHORS.stream()
                .map(author -> {
                    AuthorDTO createdAuthor = authorService.addAuthor(author);
                    log.info("A new author has been created: {}", createdAuthor);
                    return createdAuthor;
                }).collect(Collectors.toMap(AuthorDTO::fullName, a -> a));
    }

    private Set<Long> findBookAuthors(String bookTitle, Map<String, AuthorDTO> authorMap) {
        String authorName = BOOK_TO_AUTHOR.get(bookTitle);
        if (authorName == null) {
            return Set.of();
        }

        AuthorDTO author = authorMap.get(authorName);
        if (author == null) {
            return Set.of();
        }

        return Set.of(author.id());
    }


    private Set<Long> findBookGenres(String bookTitle, Map<String, GenreDTO> genreMap) {
        List<String> genreNames = BOOK_TO_GENRES.getOrDefault(bookTitle, List.of());
        return genreNames.stream()
                .map(genreMap::get)
                .filter(Objects::nonNull)
                .map(GenreDTO::id)
                .collect(Collectors.toSet());
    }
}
