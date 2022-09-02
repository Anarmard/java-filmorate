package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@EqualsAndHashCode(of = "ratingMpaId")
public class Mpa {
    private Long ratingMpaId;
    private String description;
}
