package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.time.Month.DECEMBER;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
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
        if (film.getName().isBlank()) {
            throw new ValidationException("Отсутствует название фильма");
        } else if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким ID уже существует");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 1))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        } else if (film.getDescription().length()>200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не положительная");
        }
        else {
            film.setId(getNextId()); // присваиваем новый ID
            films.put(film.getId(), film);
            log.debug("Добавлен следующий фильм {}", film);
            return films.get(film.getId());
        }
    }

    @PutMapping // обновление фильма
    public Film updateFilm(@RequestBody Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Отсутствует название фильма");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 1))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        } else if (film.getDescription().length()>200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не положительная");
        } else if (!films.containsKey(film.getId())) {
            throw new ValidationException("Указан неверный ID фильма");
        } else {
            films.put(film.getId(), film);
            return films.get(film.getId());
        }
    }

    void validateFilm (Film film) {
        if (film.getName() == null) {
            throw new ValidationException("Отсутствует название фильма");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 1))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        }
        if (film.getDescription().length()>200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не положительная");
        }
    }
}
