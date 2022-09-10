package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    // будут определены методы добавления, удаления и модификации объектов

    Collection<Genre> findAllGenres();

    Optional<Genre> getGenreByID(Long id);
}
