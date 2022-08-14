package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    // перенесите туда всю логику хранения, обновления и поиска объектов.

    private final Map<Long, Film> filmMap = new HashMap<>();
    private Long lastUsedId = 1L;

    private Long getNextId() {
        return lastUsedId++;
    }

    // получение всех фильмов
    @Override
    public Collection<Film> findAllFilms() {
        log.info("Количество фильмов: {}", filmMap.size());
        return filmMap.values();
    }

    // получение фильма по ID
    @Override
    public Film getFilmByID(Long filmId) {
        Film film = filmMap.get(filmId);
        if (film == null) {
            throw new NotFoundException("User with id=" + filmId + " not found");
        }
        return film;
    }

    // создание фильма
    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId()); // присваиваем новый ID
        filmMap.put(film.getId(), film);
        log.debug("Добавлен фильм {}", film);
        return filmMap.get(film.getId());
    }

    // обновление данных о фильме
    @Override
    public Film updateFilm(Film film) {
        if (!filmMap.containsKey(film.getId())) {
            throw new NotFoundException("Указан неверный ID фильма");
        }
        filmMap.put(film.getId(), film);
        log.debug("Обновлен фильм {}", film);
        return filmMap.get(film.getId());
    }

    // пользователь ставит лайк фильму
    @Override
    public void addLike(Film film, User user) {
        // Пусть пока каждый пользователь может поставить лайк фильму только один раз.
        log.debug("Пользователь {} поставил лайк фильму {}", user, film);
        film.getLikeUserID().add(user.getId());
    }

    // пользователь удаляет лайк
    @Override
    public void deleteLike(Film film, User user) {
        log.debug("Пользователь {} удалил лайк фильму {}", user, film);
        film.getLikeUserID().remove(user.getId());
    }

    // возвращает список из первых count фильмов по количеству лайков
    @Override
    public List<Film> getPopularFilms(Integer count) {
        log.debug("Отображается {} самых популярных фильмов", count);
        List<Film> listAllFilms = new ArrayList<>(filmMap.values());
        return listAllFilms.stream()
                .sorted((p0, p1) -> compare(p0, p1))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        return p1.getLikeUserID().size() - p0.getLikeUserID().size(); // именно в таком порядке
    }
}
