package models.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class Author {

    public Integer authorId;
    public String first;
    public String second;
    public String nationality;
    public String date;
    public String country;
    public String city;
    public String description;
}
