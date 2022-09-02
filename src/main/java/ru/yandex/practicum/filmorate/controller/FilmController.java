package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.Constants.FIRST_RELEASE_DATE;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping // получение списка всех фильмов
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}") // получение фильма по ID
    public Optional<Film> getFilmByID(@PathVariable Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой ID");
        }
        checkFilmId(id);
        return filmService.getFilmByID(id);
    }

    @PostMapping // добавление фильма
    public Optional<Film> createFilm(@RequestBody Film film) {
        validateFilm(film); // проверяем параметры фильма
        return filmService.createFilm(film);
    }

    @PutMapping // обновление данных о фильме
    public Optional<Film> updateFilm(@RequestBody Film film) {
        validateFilm(film); // проверяем параметры фильма
        Optional<Film> filmResponse = filmService.updateFilm(film);
        if (filmResponse.isEmpty()) {
            throw new NotFoundException("Указан неверный ID фильма");
        }
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}") // пользователь ставит лайк фильму
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}") // пользователь удаляет лайк
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular") // возвращает список из первых count фильмов по количеству лайков
    // Если значение параметра count не задано, верните первые 10.
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

    public void validateFilm (Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Отсутствует название фильма");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(FIRST_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        }
        if (film.getDescription() == null ||  film.getDescription().length()>200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не положительная");
        }
    }

    public void checkFilmId(Long filmId) {
        Optional<Film> filmResponse = filmService.getFilmByID(filmId);
        if (filmResponse.isEmpty()) {
            throw new NotFoundException("Film with id=" + filmId + " not found");
        }
    }

    public void checkUserId(Long userId) {
        Optional<User> userResponse = userService.getUserByID(userId);
        if (userResponse.isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }

}
