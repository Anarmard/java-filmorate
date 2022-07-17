package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id; // целочисленный идентификатор
    private String email; // электронная почта
    private String login; // логин пользователя
    private String name;// имя для отображения
    private LocalDate birthday;// дата рождения

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
