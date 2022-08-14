package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id; // целочисленный идентификатор
    private String email; // электронная почта
    private String login; // логин пользователя
    private String name;// имя для отображения
    private LocalDate birthday;// дата рождения
    private Set<Long> friendsID = new HashSet<>(); // список id друзей

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
