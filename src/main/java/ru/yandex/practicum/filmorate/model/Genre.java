package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "genreId")
public class Genre {
    private Long genreId;
    private String name;
}
