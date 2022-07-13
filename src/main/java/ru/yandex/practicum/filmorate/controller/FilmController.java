package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.time.Month.DECEMBER;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, DECEMBER, 1);
    private Long lastUsedId = 1L;

    private Long getNextId() {
        return lastUsedId++;
    }

    @GetMapping // получение всех фильмов
    public Collection<Film> findAllFilms() {
        log.info("Количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping // добавление фильма
    public Film createFilm(@RequestBody Film film) {
        FilmController filmController = new FilmController();
        filmController.validateFilm(film); // проверяем параметры фильма
        film.setId(getNextId()); // присваиваем новый ID
        films.put(film.getId(), film);
        log.debug("Добавлен фильм {}", film);
        return films.get(film.getId());
    }

    @PutMapping // обновление фильма
    public Film updateFilm(@RequestBody Film film) {
        FilmController filmController = new FilmController();
        filmController.validateFilm(film); // проверяем параметры фильма
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Указан неверный ID фильма");
        }
        films.put(film.getId(), film);
        log.debug("Обновлен фильм {}", film);
        return films.get(film.getId());
    }

    private void validateFilm (Film film) {
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
}
