package service;

import client.HttpClient;
import io.restassured.response.Response;
import models.QueryOptions;
import models.genre.Genre;
import utils.EndpointBuilder;

public class GenreService {

    public Response getGenres(QueryOptions options) {
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("genres");
        if (options.orderType != null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return HttpClient.get(endpoint.build());
    }

    public Response createGenre(Genre genre) {
        String endpoint = new EndpointBuilder().pathParameter("genre").build();
        return HttpClient.post(endpoint, genre);
    }

    public Response deleteGenre(int genreId) {
        String endpoint = new EndpointBuilder().pathParameter("genre").pathParameter(genreId).build();
        return HttpClient.delete(endpoint);
    }

    public Response updateGenre(int genreId, Genre genre) {
        String endpoint = new EndpointBuilder().pathParameter("genre").pathParameter(genreId).build();
        return HttpClient.put(endpoint, genre);
    }

    public Response getGenreById(QueryOptions options, Integer id) {
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("genre").pathParameter(id);
        if (options.orderType != null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return HttpClient.get(endpoint.build());
    }
}
