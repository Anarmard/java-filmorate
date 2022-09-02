package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id; // целочисленный идентификатор
    private String name;// имя для отображения
    private String login; // логин пользователя
    private String email; // электронная почта
    private LocalDate birthday;// дата рождения
}
