package service;

import client.HttpClient;
import io.restassured.response.Response;
import models.QueryOptions;
import models.book.Book;
import models.genre.Genre;
import utils.EndpointBuilder;

public class BookService {

    public Response getBooks(QueryOptions options) {
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("books");
        if (options.orderType != null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return HttpClient.get(endpoint.build());
    }

    public Response createBook(Book book) {
        String endpoint = new EndpointBuilder().pathParameter("book").build();
        return HttpClient.post(endpoint, book);
    }

    public Response deleteBook(int bookId) {
        String endpoint = new EndpointBuilder().pathParameter("book").pathParameter(bookId).build();
        return HttpClient.delete(endpoint);
    }

    public Response getBookById(QueryOptions options, Integer id) {
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("book").pathParameter(id);
        if (options.orderType != null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return HttpClient.get(endpoint.build());
    }
}
