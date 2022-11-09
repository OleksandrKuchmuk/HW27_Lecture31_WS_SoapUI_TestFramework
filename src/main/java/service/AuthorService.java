package service;

import client.HttpClient;
import io.restassured.response.Response;
import models.QueryOptions;
import models.author.Author;
import models.genre.Genre;
import utils.EndpointBuilder;

public class AuthorService {

    public Response getAuthors(QueryOptions options){
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("authors");
        if (options.orderType !=null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return HttpClient.get(endpoint.build());
    }

    public Response createAuthor(Author author) {
        String endpoint = new EndpointBuilder().pathParameter("author").build();
        return HttpClient.post(endpoint, author);
    }

    public Response deleteAuthor(int authorId) {
        String endpoint = new EndpointBuilder().pathParameter("author").pathParameter(authorId).build();
        return HttpClient.delete(endpoint);
    }

    public Response updateAuthor(int authorId, Author author) {
        String endpoint = new EndpointBuilder().pathParameter("author").pathParameter(authorId).build();
        return HttpClient.put(endpoint, author);
    }

    public Response getAuthorById(QueryOptions options, Integer id) {
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("author").pathParameter(id);
        if (options.orderType != null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return HttpClient.get(endpoint.build());
    }
}
