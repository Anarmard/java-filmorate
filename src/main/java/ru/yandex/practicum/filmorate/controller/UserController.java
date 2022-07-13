package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastUsedId = 1L;

    private Long getNextId() {
        return (lastUsedId++);
    }

    @GetMapping // получение списка всех пользователей.
    public Collection<User> findAllUsers() {
        log.info("Количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping // создание пользователя
    public User createUser(@RequestBody User user) {
        UserController userController = new UserController();
        userController.validateUser(user); // проверяем данные пользователя
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId()); // присваиваем новый ID
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь {}", user);
        return users.get(user.getId());
    }

    @PutMapping // обновление пользователя
    public User updateUser(@RequestBody User user) {
        UserController userController = new UserController();
        userController.validateUser(user); // проверяем данные пользователя
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Указан неверный ID пользователя");
        }
        users.put(user.getId(), user);
        log.debug("Обновлены данные пользователя {}", user);
        return users.get(user.getId());
    }

    void validateUser (User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и/или содержать пробелы!");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }
}
