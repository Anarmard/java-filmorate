package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "ratingMpaId")
public class Mpa {
    private int ratingMpaId;
    private String description;
}
