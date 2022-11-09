package api;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import models.QueryOptions;
import models.author.Author;
import models.author.Birth;
import models.author.Name;
import models.book.Additional;
import models.book.Book;
import models.book.Size;
import models.genre.Genre;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.AuthorService;
import service.BookService;
import service.GenreService;
import utils.ResponseToModel;
import utils.Validator;

import static org.apache.http.HttpStatus.*;

public class BookTests {
    private final BookService bookService = new BookService();
    private final GenreService genreService = new GenreService();
    private final AuthorService authorService = new AuthorService();
    private final ResponseToModel responseToModel = new ResponseToModel();
    public Validator validator = new Validator();
    private Genre genreResponse;
    private Author authorResponse;
    private Book bookResponse;
    Response responseCreateBook;

    @BeforeGroups("GenreAndAuthorForBook")
    public void CreateGenreAndCreateAuthorBeforeMethod(){
        RestAssured.defaultParser = Parser.JSON;
        Response responseCreateGenre = genreService.createGenre(Genre.builder().name("undefined").build());
        genreResponse = responseToModel.getAsGenreClass(responseCreateGenre);
        validator.validateStatusCode(responseCreateGenre.getStatusCode(), SC_CREATED);
        Name newAuthorName = new Name("Friderico", "Felini");
        Response responseCreateAuthor = authorService.createAuthor(Author.builder()
                .name(newAuthorName).nationality("Italian")
                .birth(new Birth("1814-03-09", "Italy", "Rome"))
                .description("it is a joke").build());
        authorResponse = responseToModel.getAsAuthorClass(responseCreateAuthor);
        validator.validateStatusCode(responseCreateAuthor.getStatusCode(), SC_CREATED);
    }

    @Test(description = "Positive check for creation a book", groups="GenreAndAuthorForBook")
    public void verifyPostCreateBook() {
        Response responseCreateBook = bookService.createBook(authorResponse.authorId, genreResponse.genreId, Book.builder()
                .bookName("Three little pigs").bookDescription("i do not know")
                .bookLanguage("English")
                .additional(new Additional(452, new Size(125, 52, 41)))
                .publicationYear(2015).build());
        bookResponse = responseToModel.getAsBookClass(responseCreateBook);
        validator.validateStatusCode(responseCreateBook.getStatusCode(), SC_CREATED)
                .validateObjectName("Book", bookResponse.bookName, "Three little pigs");

    }

    @Test(description = "Positive check for search books")
    public void verifyGetSearchBooks() {
        int expectedSize = 2;
        Response response = bookService.getBooks(new QueryOptions(1, true, expectedSize));
        validator
                .validateStatusCode(response.getStatusCode(), SC_OK)
                .validateObjectCount(responseToModel.getAsBookClassArray(response), expectedSize);
    }

    @Test(description = "Negative check for creation a book")
    public void verifyPostErrorCreateBook() {
        Response responseCreateEntity = bookService.createBook(1, 2, Book.builder().build());
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(), SC_BAD_REQUEST);
    }

    @Test(description = "Positive check for delete book", groups = "GenreAndAuthorForBook")
    public void verifyDeletebook() {
        Response responseCreateBook = bookService.createBook(authorResponse.authorId, genreResponse.genreId, Book.builder()
                .bookName("Predator").bookDescription("best film")
                .bookLanguage("English")
                .additional(new Additional(452, new Size(125, 52, 41)))
                .publicationYear(1999).build());
       Book response = responseToModel.getAsBookClass(responseCreateBook);
        Integer bookId = response.bookId;
        bookService.deleteBook(bookId);

        Response responseCheckEntity = bookService.getBookById(new QueryOptions(), bookId);
        validator
                .validateStatusCode(responseCheckEntity.getStatusCode(), SC_NOT_FOUND);
    }

    @Test(description = "Positive check for update book", groups = "GenreAndAuthorForBook")
    public void verifyUpdateBook(){
        Response responseCreateBook = bookService.createBook(authorResponse.authorId, genreResponse.genreId, Book.builder()
                .bookName("Lord of the rings").bookDescription("fantasy")
                .bookLanguage("English")
                .additional(new Additional(452, new Size(125, 52, 41)))
                .publicationYear(1970).build());
        bookResponse = responseToModel.getAsBookClass(responseCreateBook);

        Response responseUpdateBook = bookService.updateBook(bookResponse.bookId, Book.builder()
                .bookName("Terminator").bookDescription("fantastic")
                .bookLanguage("English")
                .additional(new Additional(452, new Size(125, 52, 41)))
                .publicationYear(1970).build());
        bookResponse = responseToModel.getAsBookClass(responseUpdateBook);
        validator.validateStatusCode(responseUpdateBook.getStatusCode(), SC_OK)
                .validateObjectName("Book", bookResponse.bookName, "Terminator");
    }

    @AfterGroups("GenreAndAuthorForBook")
    public void deleteGenreAndAuthorAfterGroupOfTests(){
        bookService.deleteBook(bookResponse.bookId);
        genreService.deleteGenre(genreResponse.genreId);
        authorService.deleteAuthor(authorResponse.authorId);
    }
}
