package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    // получение списка всех жанров
    public Collection<Genre> findAllGenres() {
        return genreStorage.findAllGenres();
    }

    // получение жанра по ID
    public Optional<Genre> getGenreByID(Long id) {
        return genreStorage.getGenreByID(id);
    }

}
