package service;

import client.HttpClient;
import io.restassured.response.Response;
import models.QueryOptions;
import models.book.Book;
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

    public Response createBook(int authorId, int genreId, Book book) {
        String endpoint = new EndpointBuilder().pathParameter("book").queryParam("authorId", authorId).queryParam("genreId", genreId).build();
        return HttpClient.post(endpoint, book);
    }

    public Response deleteBook(int bookId) {
        String endpoint = new EndpointBuilder().pathParameter("book").pathParameter(bookId).build();
        return HttpClient.delete(endpoint);
    }

    public Response updateBook(int bookId, Book book) {
        String endpoint = new EndpointBuilder().pathParameter("book").pathParameter(bookId).build();
        return HttpClient.put(endpoint, book);
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
