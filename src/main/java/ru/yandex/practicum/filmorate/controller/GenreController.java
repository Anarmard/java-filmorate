package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping // получение списка всех жанров
    public Collection<Genre> findAllGenres() {
        return genreService.findAllGenres();
    }

    @GetMapping("/{id}") // получение жанра по ID
    public Optional<Genre> getGenreByID(@PathVariable Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой ID");
        }
        return genreService.getGenreByID(id);
    }
}
