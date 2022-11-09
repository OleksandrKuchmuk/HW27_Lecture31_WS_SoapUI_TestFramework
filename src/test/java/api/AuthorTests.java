package api;

import io.restassured.response.Response;
import models.QueryOptions;
import models.author.Author;
import models.author.Birth;
import models.author.Name;
import org.testng.annotations.Test;
import service.AuthorService;
import utils.ResponseToModel;
import utils.Validator;

import static org.apache.http.HttpStatus.*;

public class AuthorTests {
    private final AuthorService authorService = new AuthorService();
    private final ResponseToModel responseToModel = new ResponseToModel();
    public Validator validator = new Validator();

    @Test(description = "Positive check for creation an Author")
    public void verifyPostCreateAuthor() {
        Name newAuthorName = new Name("Viliam", "Shekespeare");
        Response responseCreateEntity = authorService.createAuthor(Author.builder()
                .name(newAuthorName)
                .nationality("American")
                .birth(new Birth("1979-03-19", "USA", "Virginia"))
                .description("1")
                .build());
        Author response = responseToModel.getAsAuthorClass(responseCreateEntity);
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(), SC_CREATED)
                .validateObjectName("Author", response.name.getFirst(), newAuthorName.getFirst());
        authorService.deleteAuthor(response.authorId);
    }

    @Test(description = "Positive check for search Authors")
    public void verifyGetSearchAuthors() {
        int expectedSize = 2;
        Response response = authorService.getAuthors(new QueryOptions(1, true, expectedSize));
        validator
                .validateStatusCode(response.getStatusCode(), SC_OK)
                .validateObjectCount(responseToModel.getAsAuthorClassArray(response), expectedSize);
    }

    @Test(description = "Negative check for creation an Author")
    public void verifyPostErrorCreateAuthor() {
        Response responseCreateEntity = authorService.createAuthor(Author.builder().build());
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(), SC_BAD_REQUEST);
    }

    @Test(description = "Positive check for delete Author")
    public void verifyDeleteAuthor() {
        Name newAuthorName = new Name("Gule", "Wern");
        Response responseCreateEntity = authorService.createAuthor(Author.builder()
                .name(newAuthorName)
                .nationality("France")
                .birth(new Birth("1963-03-19", "France", "Paris"))
                .description("no deskription")
                .build());
        Author response = responseToModel.getAsAuthorClass(responseCreateEntity);
        Integer authorId = response.authorId;
        authorService
                .deleteAuthor(authorId);
        Response responseCheckEntity = authorService.getAuthorById(new QueryOptions(), authorId);
        validator
                .validateStatusCode(responseCheckEntity.getStatusCode(), SC_NOT_FOUND);
    }

    @Test(description = "Positive check for update Author")
    public void verifyUpdateAuthor() {
        Name newAuthorName = new Name("Vasyl", "Stus");
        Response responseCreateEntity = authorService.createAuthor(Author.builder()
                .name(newAuthorName)
                .nationality("Ukrainian")
                .birth(new Birth("1856-08-27", "Ukraine", "Nagujevychi"))
                .description("no deskription")
                .build());
        Author response = responseToModel.getAsAuthorClass(responseCreateEntity);
        Integer authorId = response.authorId;

        Name updatedAuthorName = new Name("Ivan", "Bagryanuj");
        Response updateAuthor = authorService.updateAuthor(authorId, Author.builder()
                .name(updatedAuthorName)
                .nationality("Ukraine")
                .birth(new Birth("1871-02-25", "Ukraine", "Novohrad-Volynskyi"))
                .description("no deskription")
                .build());
        Author updateResponse = responseToModel.getAsAuthorClass(updateAuthor);
        validator
                .validateStatusCode(updateAuthor.getStatusCode(), SC_OK)
                .validateObjectName("Author", updateResponse.name.getFirst(), updatedAuthorName.getFirst());
        authorService.deleteAuthor(authorId);
    }


}
