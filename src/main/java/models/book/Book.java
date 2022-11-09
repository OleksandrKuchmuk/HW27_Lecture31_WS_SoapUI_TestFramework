package models.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class Book {
    public Integer bookId;
    public String bookName;
    public String bookDescription;
    public String bookLanguage;
    public Additional additional;
    public Integer publicationYear;
}
