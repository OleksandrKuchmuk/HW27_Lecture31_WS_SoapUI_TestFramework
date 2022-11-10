package api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.QueryOptions;
import models.author.Author;
import models.author.Birth;
import models.author.Name;
import models.book.Additional;
import models.book.Book;
import models.book.Size;
import models.genre.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.AuthorService;
import service.BookService;
import service.GenreService;
import utils.ResponseToModel;
import utils.Validator;

import static constants.ConstantsForTests.BookParameters.*;
import static constants.ConstantsForTests.Dates.*;
import static constants.ConstantsForTests.Description.*;
import static constants.ConstantsForTests.Names.*;
import static constants.ConstantsForTests.NationalytyLocationsAndLanguages.*;
import static org.apache.http.HttpStatus.*;

@Epic("Regression")
@Feature("Rest-api feature in BookTests")
public class BookTests {
    private static final Logger LOGGER = LogManager.getLogger(BookTests.class);
    private final BookService bookService = new BookService();
    private final GenreService genreService = new GenreService();
    private final AuthorService authorService = new AuthorService();
    private final ResponseToModel responseToModel = new ResponseToModel();
    public Validator validator = new Validator();
    private int authorId;
    private int genreId;

    @BeforeMethod
    public void CreateAuthorAndGenreBeforeMethod() {
        authorId = createAuthor();
        genreId = createGenre();
    }

    @Step("Positive check for creation a book")
    @Test(description = "Positive check for creation a book", groups = {"GenreAndAuthorForBook", "delete"})
    public void verifyPostCreateBook() {
        LOGGER.info("!!! Test: Positive check for creation a book");
        LOGGER.info("Make request to create Book");
        Response responseCreateBook = bookService.createBook(authorId, genreId, Book.builder()
                .bookName(BOOK_NAME).bookDescription(BOOK_DESCRIPTION)
                .bookLanguage(BOOK_LANGUAGE)
                .additional(new Additional(BOOK_PAGE_COUNT, new Size(BOOK_HEIGHT, BOOK_WEIGHT, BOOK_LENGTH)))
                .publicationYear(BOOK_PUBLICATION_YEAR).build());
        LOGGER.info("Getting responce with information about status of creation Book");
        Book bookResponse = responseToModel.getAsBookClass(responseCreateBook);
        LOGGER.info("Validate StatusCode and Book name");
        validator.validateStatusCode(responseCreateBook.getStatusCode(), SC_CREATED)
                .validateObjectName("Book", bookResponse.bookName, BOOK_NAME);
        bookService.deleteBook(bookResponse.bookId);


    }

    @Step("Positive check for search books")
    @Test(description = "Positive check for search books")
    public void verifyGetSearchBooks() {
        LOGGER.info("!!! Test: Positive check for search books");
        int expectedSize = 2;
        LOGGER.info("Make request to search Books");
        Response response = bookService.getBooks(new QueryOptions(1, true, expectedSize));
        LOGGER.info("Validate StatusCode and count of Books objects");
        validator
                .validateStatusCode(response.getStatusCode(), SC_OK)
                .validateObjectCount(responseToModel.getAsBookClassArray(response), expectedSize);
    }

    @Step("Negative check for creation a book")
    @Test(description = "Negative check for creation a book")
    public void verifyPostErrorCreateBook() {
        LOGGER.info("!!! Test: Negative check for creation a book");
        LOGGER.info("Make incorrect request to get StatusCode 'Bad Request'");
        Response responseCreateBook = bookService.createBook(1, 2, Book.builder().build());
        LOGGER.info("Validate if we get StatusCode 'Bad Request'");
        validator
                .validateStatusCode(responseCreateBook.getStatusCode(), SC_BAD_REQUEST);
    }

    @Step("Positive check for delete book")
    @Test(description = "Positive check for delete book", groups = {"GenreAndAuthorForBook", "delete"})
    public void verifyDeletebook() {
        LOGGER.info("!!! Test: Positive check for delete Book");
        LOGGER.info("Make request to create Book in method 'verifyDeleteBook()'");
        Response responseCreateBook = bookService.createBook(authorId, genreId, Book.builder()
                .bookName(BOOK_NAME).bookDescription(BOOK_DESCRIPTION)
                .bookLanguage(BOOK_LANGUAGE)
                .additional(new Additional(452, new Size(125, 52, 41)))
                .publicationYear(BOOK_PUBLICATION_YEAR).build());
        LOGGER.info("Getting response about creating Book in method 'verifyDeleteBook()'");
        Book response = responseToModel.getAsBookClass(responseCreateBook);
        LOGGER.info("Getting Id of newly created Book");
        Integer bookId = response.bookId;
        LOGGER.info("Deleting newly created Book");
        bookService.deleteBook(bookId);
        LOGGER.info("Make request with authorID to check if the Book was deleted");
        Response responseCheckEntity = bookService.getBookById(new QueryOptions(), bookId);
        LOGGER.info("Validate if we get StatusCode 'Not Found'");
        validator
                .validateStatusCode(responseCheckEntity.getStatusCode(), SC_NOT_FOUND);
    }

    @Step("Positive check for update book")
    @Test(description = "Positive check for update book", groups = {"GenreAndAuthorForBook", "delete"})
    public void verifyUpdateBook() {
        LOGGER.info("!!! Test: Positive check for update Book");
        LOGGER.info("Make request to create Book");
        Response responseCreateBook = bookService.createBook(authorId, genreId, Book.builder()
                .bookName(BOOK_NAME).bookDescription(BOOK_DESCRIPTION)
                .bookLanguage(BOOK_LANGUAGE)
                .additional(new Additional(BOOK_PAGE_COUNT, new Size(BOOK_HEIGHT, BOOK_WEIGHT, BOOK_LENGTH)))
                .publicationYear(BOOK_PUBLICATION_YEAR).build());
        LOGGER.info("Getting responce with information about status of creation Book");
        Book bookResponse = responseToModel.getAsBookClass(responseCreateBook);
        validator.validateStatusCode(responseCreateBook.getStatusCode(), SC_CREATED);
        LOGGER.info("Make request to update Book");
        Response responseUpdateBook = bookService.updateBook(bookResponse.bookId, Book.builder()
                .bookName(BOOK_NEW_NAME).bookDescription(BOOK_NEW_DESCRIPTION)
                .bookLanguage(BOOK_NEW_LANGUAGE)
                .additional(new Additional(BOOK_NEW_PAGE_COUNT, new Size(BOOK_NEW_HEIGHT, BOOK_NEW_WEIGHT, BOOK_NEW_LENGTH)))
                .publicationYear(BOOK_NEW_PUBLICATION_YEAR).build());
        LOGGER.info("Getting responce with information about status of update Book");
        Book bookUpdateResponse = responseToModel.getAsBookClass(responseUpdateBook);
        LOGGER.info("Validate StatusCode and new Book name");
        validator.validateStatusCode(responseUpdateBook.getStatusCode(), SC_OK)
                .validateObjectName("Book", bookUpdateResponse.bookName, BOOK_NEW_NAME);
        bookService.deleteBook(bookUpdateResponse.bookId);

    }

    @Step("delete Genre, Author and Book after some Tests")
    @AfterMethod
    public void deleteGenreAndAuthorAfterGroupOfTests() {
        LOGGER.info("!!! Delete Genre, Author and Book After Group Of Tests");
        LOGGER.info("Deleting Author by authorId");
        authorService.deleteAuthor(authorId);
        LOGGER.info("Deleting Genre by GenreId");
        genreService.deleteGenre(genreId);
    }

    private int createGenre() {
        LOGGER.info("Create Genre");
        Response responseCreateGenre = genreService.createGenre(Genre.builder().name(GENRE_NAME).build());
        Genre genreResponse = responseToModel.getAsGenreClass(responseCreateGenre);
        LOGGER.info("Validate if Genre is created");
        validator.validateStatusCode(responseCreateGenre.getStatusCode(), SC_CREATED);
        return genreResponse.genreId;
    }

    private int createAuthor() {
        LOGGER.info("Create Author");
        Name newAuthorName = new Name(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME);
        Response responseCreateAuthor = authorService.createAuthor(Author.builder()
                .name(newAuthorName).nationality(AUTHOR_NATIONALITY)
                .birth(new Birth(AUTHOR_BIRTH_DATE, AUTHOR_BIRTH_COUNTRY, AUTHOR_BIRTH_CITY))
                .description(AUTHOR_DESCRIPTION).build());
        Author authorResponse = responseToModel.getAsAuthorClass(responseCreateAuthor);
        LOGGER.info("Validate if Author is created");
        validator.validateStatusCode(responseCreateAuthor.getStatusCode(), SC_CREATED);
        return authorResponse.authorId;
    }
}
