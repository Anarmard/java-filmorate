package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping // получение списка всех рейтингов
    public Collection<Mpa> findAllGenres() {
        return mpaService.findAllMpas();
    }

    @GetMapping("/{id}") // получение рейтинга по ID
    public Mpa getGenreByID(@PathVariable Long id) {
        if (id == 0) {
            throw new ValidationException("Передан пустой ID");
        }
        return mpaService.getMpaByID(id).orElseThrow(() -> new NotFoundException("MPA with id=" + id + " not found"));
    }
}
