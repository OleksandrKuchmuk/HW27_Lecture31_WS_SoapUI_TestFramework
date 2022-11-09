package utils;

import io.restassured.response.Response;
import models.author.Author;
import models.book.Book;
import models.genre.Genre;

import java.util.Arrays;
import java.util.List;

public class ResponseToModel {
    public List<Object> getAsGenreClassArray(Response response) {
        return Arrays.asList(response.getBody().as(Genre[].class));
    }

    public List<Object> getAsAuthorClassArray(Response response) {
        return Arrays.asList(response.getBody().as(Author[].class));
    }

    public List<Object> getAsBookClassArray(Response response) {
        return Arrays.asList(response.getBody().as(Book[].class));
    }

    public Genre getAsGenreClass(Response response) {
        return response.body().as(Genre.class);
    }

    public Author getAsAuthorClass(Response response) {
        return response.body().as(Author.class);
    }

    public Book getAsBookClass(Response response) {
        return response.body().as(Book.class);
    }
}
