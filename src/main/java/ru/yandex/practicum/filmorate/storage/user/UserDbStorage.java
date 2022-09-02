package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                    userRows.getDate("BIRTHDAY").toLocalDate());
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
    public Optional<User> createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");

        log.debug("Добавлен пользователь {}", user);
        return getUserByID(simpleJdbcInsert.executeAndReturnKey(toMapUser(user)).longValue());
    }

    private Map<String, Object> toMapUser(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("USER_NAME", user.getName());
        values.put("LOGIN", user.getLogin());
        values.put("EMAIL", user.getEmail());
        values.put("BIRTHDAY", user.getBirthday());
        return values;
    }

    // обновление данных о пользователе
    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery = "update USERS set " +
                "EMAIL = ? , LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
                "where USER_ID = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        log.debug("Обновлены данные пользователя {}", user);
        return getUserByID(user.getId());
    }

    // добавление в друзья
    @Override
    public void addFriend(Long userId, Long friendId) {
        // для прохождения теста, надо проверить есть ли уже такой друг в списке друзей у user
        String sqlExistingCheck = "select USER_ID from FRIENDLISTS where (USER_ID = ?) and (FRIEND_ID = ?)";

        if (jdbcTemplate.queryForRowSet(sqlExistingCheck, userId, friendId).next()) {
            throw new ValidationException("Друг с таким id уже есть в списке друзей пользователя.");
        }

        String sqlQuery = "insert into FRIENDLISTS(USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    // удаление из друзей
    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        String sqlExistingCheck = "select USER_ID from FRIENDLISTS where (USER_ID = ?) and (FRIEND_ID = ?)";

        if (!jdbcTemplate.queryForRowSet(sqlExistingCheck, userId, friendId).next()) {
            throw new ValidationException("Друга с таким id нет в списке друзей пользователя.");
        }

        String sqlQuery = "delete from FRIENDLISTS where (USER_ID = ?) and (FRIEND_ID = ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return true;
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
