package utils;

import org.testng.Assert;

import java.util.List;

public class Validator {
    public Validator validateStatusCode(int actualCode, int expectedCode) {
        Assert.assertEquals(actualCode, expectedCode, String.format(
                "\nStatus code is: '%s'.\nExpected: %s", actualCode, expectedCode));
        return this;
    }

    public Validator validateObjectName(String typeOfObject, String actualName, String expectedName) {
        Assert.assertEquals(actualName, expectedName, String.format(
                "\n" + typeOfObject + " Name is: '%s'.\nExpected: %s", actualName, expectedName));
        return this;
    }

    public Validator validateObjectCount(List<Object> objects, int expected) {
        Assert.assertEquals(objects.size(), expected);
        return this;
    }
}