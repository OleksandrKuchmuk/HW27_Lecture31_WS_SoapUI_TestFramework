package utils;

import io.restassured.response.Response;
import models.genre.Genre;

import java.util.Arrays;
import java.util.List;

public class ResponseToModel {
    public List<Genre> getAsGenreClassArray(Response response) {
        return Arrays.asList(response.getBody().as(Genre[].class));
    }

    public Genre getAsGenreClass(Response response) {
        return response.body().as(Genre.class);
    }
}
