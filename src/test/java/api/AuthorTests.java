package api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.QueryOptions;
import models.author.Author;
import models.author.Birth;
import models.author.Name;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import service.AuthorService;
import utils.ResponseToModel;
import utils.Validator;

import static constants.ConstantsForTests.Dates.AUTHOR_BIRTH_DATE;
import static constants.ConstantsForTests.Dates.AUTHOR_NEW_BIRTH_DATE;
import static constants.ConstantsForTests.Description.AUTHOR_DESCRIPTION;
import static constants.ConstantsForTests.Description.AUTHOR_NEW_DESCRIPTION;
import static constants.ConstantsForTests.Names.*;
import static constants.ConstantsForTests.NationalytyLocationsAndLanguages.*;
import static org.apache.http.HttpStatus.*;

@Epic("Regression")
@Feature("Rest-api feature in AuthorTests")
public class AuthorTests {
    private static final Logger LOGGER = LogManager.getLogger(AuthorTests.class);
    private final AuthorService authorService = new AuthorService();
    private final ResponseToModel responseToModel = new ResponseToModel();
    public Validator validator = new Validator();
    Author response;

    @Step("Positive check for creation an Author")
    @Test(description = "Positive check for creation an Author")
    public void verifyPostCreateAuthor() {
        LOGGER.info("!!! Test: Make positive Test of creation an Author");
        LOGGER.info("Make request to create Author");
        Response responseCreateEntity = authorService.createAuthor(Author.builder()
                .name(new Name(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME))
                .nationality(AUTHOR_NATIONALITY)
                .birth(new Birth(AUTHOR_BIRTH_DATE, AUTHOR_BIRTH_COUNTRY, AUTHOR_BIRTH_CITY))
                .description(AUTHOR_DESCRIPTION)
                .build());
        LOGGER.info("Getting responce with information about status of creation Author");
        response = responseToModel.getAsAuthorClass(responseCreateEntity);
        LOGGER.info("Validate StatusCode and Author name");
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(), SC_CREATED)
                .validateObjectName("Author", response.name.getFirst(), AUTHOR_FIRST_NAME);
    }

    @Step("Positive check for search Authors")
    @Test(description = "Positive check for search Authors")
    public void verifyGetSearchAuthors() {
        LOGGER.info("!!! Test: Make positive check for search Authors");
        int expectedSize = 2;
        LOGGER.info("Make request to search authors");
        Response response = authorService.getAuthors(new QueryOptions(1, true, expectedSize));
        LOGGER.info("Validate StatusCode and count of Authors objects");
        validator
                .validateStatusCode(response.getStatusCode(), SC_OK)
                .validateObjectCount(responseToModel.getAsAuthorClassArray(response), expectedSize);
    }

    @Step("Negative check for creation an Author")
    @Test(description = "Negative check for creation an Author")
    public void verifyPostErrorCreateAuthor() {
        LOGGER.info("!!! Test: Negative check for creation an Author");
        LOGGER.info("Make incorrect request to get StatusCode 'Bad Request'");
        Response responseCreateEntity = authorService.createAuthor(Author.builder().build());
        LOGGER.info("Validate if we get StatusCode 'Bad Request'");
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(), SC_BAD_REQUEST);
    }

    @Step("Positive check for delete Author")
    @Test(description = "Positive check for delete Author")
    public void verifyDeleteAuthor() {
        LOGGER.info("!!! Test: Positive check for delete Author");
        LOGGER.info("Create object of Name.class");
        Name newAuthorName = new Name(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME);
        LOGGER.info("Make request to create author in method 'verifyDeleteAuthor()'");
        Response responseCreateEntity = authorService.createAuthor(Author.builder()
                .name(newAuthorName)
                .nationality(AUTHOR_NATIONALITY)
                .birth(new Birth(AUTHOR_BIRTH_DATE, AUTHOR_BIRTH_COUNTRY, AUTHOR_BIRTH_CITY))
                .description(AUTHOR_DESCRIPTION)
                .build());
        LOGGER.info("Getting response about creating author in method 'verifyDeleteAuthor()'");
        response = responseToModel.getAsAuthorClass(responseCreateEntity);
        LOGGER.info("Getting Id of newly created Author");
        Integer authorId = response.authorId;
        LOGGER.info("Deleting newly created Author");
        authorService.deleteAuthor(authorId);
        LOGGER.info("Make request with authorID to check if the Author was deleted");
        Response responseCheckEntity = authorService.getAuthorById(new QueryOptions(), authorId);
        LOGGER.info("Validate if we get StatusCode 'Not Found'");
        validator
                .validateStatusCode(responseCheckEntity.getStatusCode(), SC_NOT_FOUND);
    }

    @Step("Positive check for update Author")
    @Test(description = "Positive check for update Author")
    public void verifyUpdateAuthor() {
        LOGGER.info("!!! Test: Positive check for update Author");
        LOGGER.info("Create object of Name.class");
        Name newAuthorName = new Name(AUTHOR_FIRST_NAME, AUTHOR_LAST_NAME);
        LOGGER.info("Make request to create Author");
        Response responseCreateEntity = authorService.createAuthor(Author.builder()
                .name(newAuthorName)
                .nationality(AUTHOR_NATIONALITY)
                .birth(new Birth(AUTHOR_BIRTH_DATE, AUTHOR_BIRTH_COUNTRY, AUTHOR_BIRTH_CITY))
                .description(AUTHOR_DESCRIPTION)
                .build());
        LOGGER.info("Getting responce with information about status of creation Author");
        response = responseToModel.getAsAuthorClass(responseCreateEntity);
        Integer authorId = response.authorId;
        LOGGER.info("Create object of Name.class with parameters to update");
        LOGGER.info("Make request to update Author");
        Response updateAuthor = authorService.updateAuthor(authorId, Author.builder()
                .name(new Name(AUTHOR_NEW_FIRST_NAME, AUTHOR_NEW_LAST_NAME))
                .nationality(AUTHOR_NEW_NATIONALITY)
                .birth(new Birth(AUTHOR_NEW_BIRTH_DATE, AUTHOR_NEW_BIRTH_COUNTRY, AUTHOR_NEW_BIRTH_CITY))
                .description(AUTHOR_NEW_DESCRIPTION)
                .build());
        LOGGER.info("Getting responce with information about status of update Author");
        Author updateResponse = responseToModel.getAsAuthorClass(updateAuthor);
        LOGGER.info("Validate StatusCode and new Author name");
        validator
                .validateStatusCode(updateAuthor.getStatusCode(), SC_OK)
                .validateObjectName("Author", updateResponse.name.getFirst(), AUTHOR_NEW_FIRST_NAME);
    }

    @Step("Deleting Authors after Test")
    @AfterMethod
    public void deleteAuthor() {
        LOGGER.info("Delete Author after creating Author for test");
        authorService.deleteAuthor(response.authorId);
    }
}