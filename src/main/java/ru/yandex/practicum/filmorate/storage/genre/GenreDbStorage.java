package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        // используем конструктор, методы ResultSet и готовое значение genre
        Long id = rs.getLong("GENRE_ID");
        String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }

    // получение списка всех жанров
    @Override
    public Collection<Genre> findAllGenres() {
        // выполняем запрос к базе данных
        String sql = "select * from GENRES";
        return jdbcTemplate.query(sql, (rs,rowNum) -> makeGenre(rs));
    }

    // получение пользователя по ID
    @Override
    public Optional<Genre> getGenreByID(Long id) {
        // выполняем запрос к базе данных
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select * from GENRES where GENRE_ID = ?", id);

        // обрабатываем результат выполнения запроса
        if(genresRows.next()) {
            Genre genre = new Genre(
                    genresRows.getLong("GENRE_ID"),
                    genresRows.getString("GENRE_NAME"));
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}