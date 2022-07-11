package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    static FilmController filmController = new FilmController();
    Film film = new Film(1L,"Green", "description1",
            LocalDate.of(2005, 8,1), 120);

    @BeforeEach
    void filmCreate() {
        film.setId(1L);
        film.setName("Green");
        film.setDescription("description1");
        film.setReleaseDate(LocalDate.of(2005, 8,1));
        film.setDuration(120);
    }

    @Test
    void nameIsNull() {
        film.setName(null);
        Exception exception = assertThrows(ValidationException.class, ()-> filmController.validateFilm(film));
        assertEquals("Отсутствует название фильма", exception.getMessage());
    }

    @Test
    void releaseDateIs1800() {
        film.setReleaseDate(LocalDate.of(1800, 1,1));
        Exception exception = assertThrows(ValidationException.class, ()-> filmController.validateFilm(film));
        assertEquals("Дата релиза раньше 28.12.1895", exception.getMessage());
    }

    @Test
    void descriptionIsMore200() {
        film.setDescription("ergreg ew fewg weg wefewg we ef weef we ewf wef wef wef wef wef eef wef wef" +
                "w efew fwe f wef wef wef wef we fwe fwef wef wef wef wef wef we" +
                "wef wf wef ewf wf wef weef wef wef ewfwe f wf wef wef wef wef wef we wef wef wef" +
                "we fwef wef wef wef weef weef wef wef wef wef wef we few fewe wfe wef we fwef ew" +
                "we fwe fwefwe fwe fwe few wef we fwe fwe fwef we ffew few ew we fwef wef wef wefew  wefewf ");
        Exception exception = assertThrows(ValidationException.class, ()-> filmController.validateFilm(film));
        assertEquals("максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    void durationIs0() {
        film.setDuration(0);
        Exception exception = assertThrows(ValidationException.class, ()-> filmController.validateFilm(film));
        assertEquals("Продолжительность фильма не положительная", exception.getMessage());
    }

    @Test
    void durationIsNegative() {
        film.setDuration(-1);
        Exception exception = assertThrows(ValidationException.class, ()-> filmController.validateFilm(film));
        assertEquals("Продолжительность фильма не положительная", exception.getMessage());
    }
}
