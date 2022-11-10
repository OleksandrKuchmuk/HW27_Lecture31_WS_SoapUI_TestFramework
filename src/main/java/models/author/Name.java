package models.author;


import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class Name {
    public String first;
    public String last;
}