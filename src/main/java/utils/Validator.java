package utils;

import io.restassured.response.Response;
import models.genre.Genre;
import org.testng.Assert;

import java.util.List;

public class Validator {
        public Validator validateStatusCode(int actualCode, int expectedCode) {
                Assert.assertEquals(actualCode, expectedCode, String.format(
                        "\nStatus code is: '%s'.\nExpected: %s", actualCode, expectedCode));
                return this;
        }

        public Validator validateGenreName(String actualName, String expectedName) {
                Assert.assertEquals(actualName, expectedName, String.format(
                        "\nGenre Name is: '%s'.\nExpected: %s", actualName, expectedName));
                return this;
        }

        public Validator validateGenresCount(List<Genre> genres, int expected) {
                Assert.assertEquals(genres.size(), expected);
                return this;
        }
}