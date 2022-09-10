package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Mpa {
    private Long id;
    private String name;

    @JsonCreator
    public Mpa(Long id) {
        this.id = id;
    }
}
