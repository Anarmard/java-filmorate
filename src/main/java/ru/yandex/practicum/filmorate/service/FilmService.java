package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

// Отвечать за операции с фильмами, — добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков.
// Пусть пока каждый пользователь может поставить лайк фильму только один раз.

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    // получение списка всех фильмов
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    // получение фильма по ID
    public Film getFilmByID(Long id) {
        checkFilmID(id);
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
        checkFilmID(filmId);
        checkUserID(userId);

        // передаем не ID, а сам фильм и пользователя. Совет наставника, на будущее при работе с БД
        Film film = filmStorage.getFilmByID(filmId);
        User user = userStorage.getUserByID(userId);
        filmStorage.addLike(film, user);
    }

    // пользователь удаляет лайк
    public void deleteLike(Long filmId, Long userId) {
        checkFilmID(filmId);
        checkUserID(userId);

        // передаем не ID, а сам фильм и пользователя. Совет наставника, на будущее при работе с БД
        Film film = filmStorage.getFilmByID(filmId);
        User user = userStorage.getUserByID(userId);
        filmStorage.deleteLike(film, user);
    }

    // возвращает список из первых count фильмов по количеству лайков
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    void checkFilmID (Long filmId) {
        Film film = filmStorage.getFilmByID(filmId);
        if (film == null) {
            throw new NotFoundException("User with id=" + filmId + " not found");
        }
    }

    public void checkUserID (Long userId) {
        User user = userStorage.getUserByID(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }

}
