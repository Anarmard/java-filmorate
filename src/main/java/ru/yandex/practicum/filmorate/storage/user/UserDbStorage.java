package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        // используем конструктор, методы ResultSet и готовое значение user
        Long id = rs.getLong("USER_ID");
        String name = rs.getString("USER_NAME");
        String login = rs.getString("LOGIN");
        String email = rs.getString("EMAIL");
        // Получаем дату и конвертируем её из sql.Date в time.LocalDate
        LocalDate birthDate = rs.getDate("BIRTHDAY").toLocalDate();
        return new User(id, name, login, email, birthDate);
    }

    // получение списка всех пользователей
    @Override
    public Collection<User> findAllUsers() {
        // выполняем запрос к базе данных
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, (rs,rowNum) -> makeUser(rs));
    }

    // получение пользователя по ID
    @Override
    public Optional<User> getUserByID(Long id) {
        // выполняем запрос к базе данных
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);

        // обрабатываем результат выполнения запроса
        if(userRows.next()) {
            User user = new User(
                    userRows.getLong("USER_ID"),
                    userRows.getString("USER_NAME"),
                    userRows.getString("LOGIN"),
                    userRows.getString("EMAIL"),
                    Objects.requireNonNull(userRows.getDate("BIRTHDAY")).toLocalDate());
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    // создание пользователя
    // если не работает, то попробовать второй вариант (сложный) из статьи
    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into USERS(USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.debug("Добавлен пользователь {}", user);
        return user;
    }

    // обновление данных о пользователе
    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery = "update USERS set " +
                "USER_NAME = ?, LOGIN = ?, EMAIL =? , BIRTHDAY = ? " +
                "where USER_ID = ?";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());

        log.debug("Обновлены данные пользователя {}", user);
        return getUserByID(user.getId());
    }

    // добавление в друзья
    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "insert into FRIENDLISTS(USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    // удаление из друзей
    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        String sqlQuery = "delete from FRIENDLISTS where (USER_ID = ?) and (FRIEND_ID = ?)";
        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

    // возвращаем список пользователей, являющихся его друзьями
    @Override
    public List<User> getListOfFriends(Long userId) {
        // выполняем запрос к базе данных
        String sql = "select u.user_id, u.user_name, u.login, u.email, u.birthday " +
                "from FRIENDLISTS as fl " +
                "join USERS as u on fl.friend_id = u.user_id " +
                "where fl.user_id = ?";

        return jdbcTemplate.query(sql, (rs,rowNum) -> makeUser(rs), userId);
    }

    // список друзей, общих с другим пользователем
    @Override
    public List<User> getListOfCommonFriends(Long userId, Long friendId) {
        // выполняем запрос к базе данных
        String sql = "select u.user_id, u.user_name, u.login, u.email, u.birthday " +
                "from FRIENDLISTS as fl " +
                "join USERS as u on fl.friend_id = u.user_id " +
                "where (fl.user_id = ?) and (fl.friend_id IN ( " +
                    "select friend_id " +
                    "from FRIENDLISTS " +
                    "where user_id = ?))";

        return jdbcTemplate.query(sql, (rs,rowNum) -> makeUser(rs), userId, friendId);
    }
}
