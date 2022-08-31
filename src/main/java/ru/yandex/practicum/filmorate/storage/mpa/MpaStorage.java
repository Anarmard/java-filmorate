package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

public interface MpaStorage {
    // будут определены методы добавления, удаления и модификации объектов

    Collection<Mpa> findAllMpas();

    Optional<Mpa> getMpaByID(int id);
}
