package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    // будут определены методы добавления, удаления и модификации объектов
    public Long getNextId();

    public Collection<Film> findAllFilms();

    public Film getFilmByID(Long id);

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getPopularFilms(Integer count);

}
