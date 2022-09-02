package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    // будут определены методы добавления, удаления и модификации объектов

    Collection<Film> findAllFilms();

    Optional<Film> getFilmByID(Long id);

    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getPopularFilms(Integer count);

}
