package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    // получение списка всех жанров
    public Collection<Mpa> findAllMpas() {
        return mpaStorage.findAllMpas();
    }

    // получение жанра по ID
    public Optional<Mpa> getMpaByID(Long id) {
        return mpaStorage.getMpaByID(id);
    }

}
