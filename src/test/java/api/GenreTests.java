package api;


import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.QueryOptions;
import models.genre.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import service.GenreService;
import utils.ResponseToModel;
import utils.Validator;

import static constants.ConstantsForTests.Names.GENRE_NAME;
import static constants.ConstantsForTests.Names.GENRE_NEW_NAME;
import static org.apache.http.HttpStatus.*;

@Epic("Regression")
@Feature("Rest-api feature in GenreTests")
public class GenreTests {
    private static final Logger LOGGER = LogManager.getLogger(GenreTests.class);
    public Validator validator = new Validator();
    private final GenreService genreService = new GenreService();
    private final ResponseToModel responseToModel = new ResponseToModel();

    @Step("Positive check for creation a genre")
    @Test (description = "Positive check for creation a genre")
    public void verifyPostCreateGenre () {
        LOGGER.info("!!! Test: Positive check for creation a genre");
//        String testName = "newGenre";
        LOGGER.info("Make request to create genre");
        Response responseCreateEntity = genreService.createGenre(Genre.builder().name(GENRE_NAME).build());
        LOGGER.info("Getting responce with information about status of creation of Genre");
        Genre response = responseToModel.getAsGenreClass(responseCreateEntity);
        LOGGER.info("Validate StatusCode and Genre name");
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(),SC_CREATED)
                .validateObjectName("Genre",response.name, GENRE_NAME);
        LOGGER.info("Deleting Genre by genreId after finish test");
        genreService.deleteGenre(response.genreId);
    }

@Step("Positive check for search genres")
    @Test (description = "Positive check for search genres")
    public void verifyGetSearchGenres () {
        LOGGER.info("!!! Test: Positive check for search genres");
        int expectedSize = 2;
        LOGGER.info("Make request to search Genres");
        Response response = genreService.getGenres(new QueryOptions(1, true, expectedSize));
        LOGGER.info("Validate StatusCode and count of Genres objects");
        validator
                .validateStatusCode(response.getStatusCode(), SC_OK)
                .validateObjectCount(responseToModel.getAsGenreClassArray(response), expectedSize);
    }

    @Step("Negative check for creation a genre")
    @Test (description = "Negative check for creation a genre")
    public void verifyPostErrorCreateGenre () {
        LOGGER.info("!!! Test: Negative check for creation a genre");
        LOGGER.info("Make incorrect request to get StatusCode 'Bad Request'");
        Response responseCreateEntity = genreService.createGenre(Genre.builder().build());
        LOGGER.info("Validate if we get StatusCode 'Bad Request'");
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(), SC_BAD_REQUEST);
    }

    @Step("Positive check for delete genre")
    @Test (description = "Positive check for delete genre")
    public void verifyDeleteGenre () {
        LOGGER.info("!!! Test: Positive check for delete Genre");
        LOGGER.info("Make request to create Genre in method 'verifyDeleteGenre()'");
        Response responseCreateEntity = genreService.createGenre(Genre.builder().name(GENRE_NAME).build());
        LOGGER.info("Getting response about creating Genre in method 'verifyDeleteGenre()'");
        Genre response = responseToModel.getAsGenreClass(responseCreateEntity);
        LOGGER.info("Getting Id of newly created Genre");
        Integer genreId = response.genreId;
        LOGGER.info("Deleting newly created Genre");
        genreService.deleteGenre(genreId);
        LOGGER.info("Make request with authorID to check if the Genre was deleted");
        Response responseCheckEntity = genreService.getGenreById(new QueryOptions(), genreId);
        LOGGER.info("Validate if we get StatusCode 'Not Found'");
        validator
                .validateStatusCode(responseCheckEntity.getStatusCode(), SC_NOT_FOUND);
    }

    @Step("Positive check for update a genre")
    @Test (description = "Positive check for update a genre")
    public void verifyPutUbdateGenre () {
        LOGGER.info("!!! Test: Positive check for update Genre");
        LOGGER.info("Make request to create Genre");
        Response responseCreateEntity = genreService.createGenre(Genre.builder().name(GENRE_NAME).build());
        LOGGER.info("Getting responce with information about status of creation Genre");
        Genre createResponse = responseToModel.getAsGenreClass(responseCreateEntity);
        LOGGER.info("Make request to update Genre");
        Response responseUpdateGenre = genreService.updateGenre(createResponse.genreId, Genre.builder().name(GENRE_NEW_NAME).build());
        LOGGER.info("Getting responce with information about status of update Genre");
        Genre updateResponse = responseToModel.getAsGenreClass(responseUpdateGenre);
        LOGGER.info("Validate StatusCode and new Genre name");
        validator
                .validateStatusCode(responseUpdateGenre.getStatusCode(),SC_OK)
                .validateObjectName("Genre",updateResponse.name, GENRE_NEW_NAME);

        LOGGER.info("Deleting Genre by genreId after test");
        genreService.deleteGenre(updateResponse.genreId);
    }
}
