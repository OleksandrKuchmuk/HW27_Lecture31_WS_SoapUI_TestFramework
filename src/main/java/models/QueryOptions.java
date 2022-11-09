package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class QueryOptions {
    public String orderType;
    public int page;
    public boolean pagination;
    public int size;
    public String sortBy;
    public int autorId;
    public int genreId;

    // setting some of the default values
    public QueryOptions() {
        this.page = 1;
        this.pagination = true;
        this.size = 10;
    }

    public QueryOptions(int page, boolean pagination, int size) {
        this.page = page;
        this.pagination = pagination;
        this.size = size;
    }

    public QueryOptions(int autorId, int genreId) {
        this.autorId = autorId;
        this.genreId = genreId;
    }
}