package api;


import io.restassured.response.Response;
import models.QueryOptions;
import models.genre.Genre;
import org.testng.annotations.Test;
import service.GenreService;
import utils.ResponseToModel;
import utils.Validator;

import static org.apache.http.HttpStatus.*;

public class GenreTests {
    public Validator validator = new Validator();
    private final GenreService genreService = new GenreService();
    private final ResponseToModel responseToModel = new ResponseToModel();

    @Test (description = "Positive check for creation a genre")
    public void verifyPostCreateGenre () {
        String testName = "test6";
        Response responseCreateEntity = genreService.createGenre(Genre.builder().name(testName).build());
        Genre response = responseToModel.getAsGenreClass(responseCreateEntity);
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(),SC_CREATED)
                .validateObjectName("Genre",response.name, testName);

        genreService.deleteGenre(response.genreId);
    }

    @Test (description = "Positive check for search genres")
    public void verifyGetSearchGenres () {
        int expectedSize = 2;
        Response response = genreService.getGenres(new QueryOptions(1, true, expectedSize));
        validator
                .validateStatusCode(response.getStatusCode(), SC_OK)
                .validateObjectCount(responseToModel.getAsGenreClassArray(response), expectedSize);
    }

    @Test (description = "Negative check for creation a genre")
    public void verifyPostErrorCreateGenre () {
        Response responseCreateEntity = genreService.createGenre(Genre.builder().build());
        validator
                .validateStatusCode(responseCreateEntity.getStatusCode(), SC_BAD_REQUEST);
    }

    @Test (description = "Positive check for delete genre")
    public void verifyDeleteGenre () {
        Response responseCreateEntity = genreService.createGenre(Genre.builder().name("testName").build());
        Genre response = responseToModel.getAsGenreClass(responseCreateEntity);
        Integer genreId = response.genreId;
        genreService
                .deleteGenre(response.genreId);

        Response responseCheckEntity = genreService.getGenreById(new QueryOptions(), genreId);
        validator
                .validateStatusCode(responseCheckEntity.getStatusCode(), SC_NOT_FOUND);
    }

    @Test (description = "Positive check for update a genre")
    public void verifyPutUbdateGenre () {
        Response responseCreateEntity = genreService.createGenre(Genre.builder().name("testName").build());
        Genre createResponse = responseToModel.getAsGenreClass(responseCreateEntity);
        Response responseUpdateGenre = genreService.updateGenre(createResponse.genreId, Genre.builder().name("newName").build());

        Genre updateResponse = responseToModel.getAsGenreClass(responseUpdateGenre);
        validator
                .validateStatusCode(responseUpdateGenre.getStatusCode(),SC_OK)
                .validateObjectName("Genre",updateResponse.name, "newName");

        genreService.deleteGenre(updateResponse.genreId);
    }
}
