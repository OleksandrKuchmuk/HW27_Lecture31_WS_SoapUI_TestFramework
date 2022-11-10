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
    public Name name;
    public String nationality;
    public Birth birth;
    public String description;


}
