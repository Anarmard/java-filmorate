package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        // используем конструктор, методы ResultSet и готовое значение mpa
        Long id = rs.getLong("RATING_MPA");
        String description = rs.getString("DESCRIPTION");
        return new Mpa(id, description);
    }

    // получение списка всех жанров
    @Override
    public Collection<Mpa> findAllMpas() {
        // выполняем запрос к базе данных
        String sql = "select * from RATING_MPA";
        return jdbcTemplate.query(sql, (rs,rowNum) -> makeMpa(rs));
    }

    // получение пользователя по ID
    @Override
    public Optional<Mpa> getMpaByID(Long id) {
        // выполняем запрос к базе данных
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from RATING_MPA where RATING_MPA_ID = ?", id);

        // обрабатываем результат выполнения запроса
        if(mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getLong("RATING_MPA_ID"),
                    mpaRows.getString("DESCRIPTION"));
            log.info("Найден рейтинг: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}