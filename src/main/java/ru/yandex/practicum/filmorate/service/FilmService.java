package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // получение списка всех фильмов
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    // получение фильма по ID
    public Optional<Film> getFilmByID(Long id) {
        return filmStorage.getFilmByID(id);
    }

    // добавление фильма
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    // обновление данных о фильме
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    // пользователь ставит лайк фильму
    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    // пользователь удаляет лайк
    public void deleteLike(Long filmId, Long userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    // возвращает список из первых count фильмов по количеству лайков
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }
}
