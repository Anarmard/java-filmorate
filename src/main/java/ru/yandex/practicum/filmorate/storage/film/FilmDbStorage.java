package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film makeFilm(ResultSet rs, Map<Long, Set<Genre>> genreMap) throws SQLException {
        // используем конструктор, методы ResultSet и готовое значение film
        Long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        // Получаем дату и конвертируем её из sql.Date в time.LocalDate
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        Mpa mpa = new Mpa(rs.getLong("RATING_MPA"), rs.getString("DESCRIPTION"));
        Set<Genre> genres = genreMap.getOrDefault(id, new HashSet<>()); // проверить можно ли так записать в Set!!!

        return new Film(id, name, description, releaseDate, duration, mpa, genres);
    }

    // cоздание Map фильм-все его жанры. Нужен для метода findAllFilms
    private Map<Long, Set<Genre>> makeGenreMap(SqlRowSet genreRows) {
        Map<Long, Set<Genre>> genreMap = new HashMap<>();

        // обрабатываем результат выполнения запроса
        while (genreRows.next()) {
            Long filmId = genreRows.getLong("FILM_ID");

            // если фильма с таким id еще нет в Map фильм-жанры
            if ((!genreMap.containsKey(filmId))) {
                genreMap.put(filmId, new HashSet<>());
            }
            // добавляем к этому фильму id & name жанра
            genreMap.get(filmId).add(new Genre(genreRows.getLong("GENRE_ID"), genreRows.getString("GENRE_NAME")));
        }
        return genreMap;
    }

    // получение всех фильмов
    @Override
    public Collection<Film> findAllFilms() {
        String sqlFilm = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, " +
                "f.DURATION, f.RATE, f.RATING_MPA, mpa.DESCRIPTION " +
                "from FILMS as f join RATING_MPA as mpa on f.RATING_MPA = mpa.RATING_MPA_ID";

        String sqlGenre = "select fg.FILM_ID, fg.GENRE_ID, g.GENRE_NAME " +
                "from FILMS_GENRES as fg join GENRES as g on fg.GENRE_ID = g.GENRE_ID";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlGenre);
        Map<Long, Set<Genre>> genreMap = makeGenreMap(genreRows);

        return jdbcTemplate.query(sqlFilm, (rs,rowNum) -> makeFilm(rs, genreMap));
    }

    // получение фильма по ID
    @Override
    public Optional<Film> getFilmByID(Long filmId) {
        // выполняем запрос к базе данных
        String sqlFilm = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, " +
                "f.DURATION, mpa.RATING_MPA_ID, mpa.DESCRIPTION " +
                "from FILMS as f join RATING_MPA as mpa on f.RATING_MPA = mpa.RATING_MPA_ID " +
                "where f.FILM_ID = ?";

        String sqlGenre = "select fg.FILM_ID, g.GENRE_ID, g.GENRE_NAME " +
                "from FILMS_GENRES as fg join GENRES as g on fg.GENRE_ID = g.GENRE_ID " +
                "where fg.FILM_ID = ?";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlGenre, filmId);
        Map<Long, Set<Genre>> genreMap = makeGenreMap(genreRows);

        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlFilm, filmId);

        // обрабатываем результат выполнения запроса
        if(filmRow.next()) {
            Film film = new Film(filmRow.getLong("FILM_ID"),
                    filmRow.getString("FILM_NAME"),
                    filmRow.getString("DESCRIPTION"),
                    filmRow.getDate("RELEASE_DATE").toLocalDate(),
                    filmRow.getInt("DURATION"),
                    new Mpa(filmRow.getLong("RATING_MPA"), filmRow.getString("DESCRIPTION")),
                    genreMap.getOrDefault(filmId, new HashSet<>())
            );
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            return Optional.empty();
        }
    }


    // создание фильма
    @Override
    public Optional<Film> createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");

        Long filmId = simpleJdbcInsert.executeAndReturnKey(toMapFilm(film)).longValue();

        // теперь надо добавить инфо о жанре в таблицу FILMS_GENRES
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) " + // или MERGE
                        "values (?,?)", filmId, genre.getGenreId());
            }
        }

        log.debug("Добавлен фильм {}", film);
        return getFilmByID(filmId);
    }

    private Map<String, Object> toMapFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("DURATION", film.getDuration());
        values.put("RATING_MPA", film.getMpa().getRatingMpaId());
        return values;
    }

    // обновление данных о фильме
    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlFilm = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE =?, DURATION = ?, RATING_MPA = ?, RATE = ?" +
                "WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getRatingMpaId(),
                film.getId());

        // надо обновить инфо о жанрах фильма, но сначала удалить все жанры по этому фильму
        jdbcTemplate.update("DELETE from FILMS_GENRES WHERE FILM_ID = ?", film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) " + // или MERGE
                        "values (?,?)", film.getId(), genre.getGenreId());
            }
        }

        log.debug("Обновлен фильм {}", film);
        return getFilmByID(film.getId());
    }

    // пользователь ставит лайк фильму
    @Override
    public void addLike(Long filmId, Long userId) {
        // добавить запись в таблицу LIKES
        String sqlAddLike = "MERGE INTO LIKES (FILM_ID, USER_ID) values ( ?,? )";
        jdbcTemplate.update(sqlAddLike, filmId, userId);

        // надо RATE в таблице FILMS обновить
        String sqlIncreaseRate = "UPDATE FILMS as f SET f.RATE = (" +
                "SELECT COUNT(l.USER_ID) " +
                "FROM LIKES as l " +
                "WHERE l.FILM_ID = ?)" +
                "WHERE f.FILM_ID = ?";
        jdbcTemplate.update(sqlIncreaseRate, filmId, filmId);
        log.debug("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    // пользователь удаляет лайк
    @Override
    public void deleteLike(Long filmId, Long userId) {
        // надо удалить запись из таблицы LIKES
        String sqlRemoveLike = "DELETE from LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlRemoveLike, filmId, userId);

        // надо RATE в таблице FILMS обновить
        String sqlDecreaseRate = "UPDATE FILMS as f SET f.RATE = (" +
                "SELECT COUNT(l.USER_ID) " +
                "FROM LIKES as l " +
                "WHERE l.FILM_ID = ?)" +
                "WHERE f.FILM_ID = ?";
        jdbcTemplate.update(sqlDecreaseRate, filmId, filmId);
        log.debug("Пользователь {} удалил лайк фильму {}", userId, filmId);
    }

    // возвращает список из первых count фильмов по количеству лайков
    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlFilm = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, " +
                "f.DURATION, f.RATE, f.RATING_MPA, mpa.DESCRIPTION " +
                "from FILMS as f join RATING_MPA as mpa on f.RATING_MPA = mpa.RATING_MPA_ID " +
                "ORDER BY f.RATE DESC " +
                "LIMIT ?";

        String sqlGenre = "select fg.FILM_ID, fg.GENRE_ID, g.GENRE_NAME " +
                "from FILMS_GENRES as fg join GENRES as g on fg.GENRE_ID = g.GENRE_ID";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlGenre);
        Map<Long, Set<Genre>> genreMap = makeGenreMap(genreRows);

        return jdbcTemplate.query(sqlFilm, (rs,rowNum) -> makeFilm(rs, genreMap), count);
    }
}
