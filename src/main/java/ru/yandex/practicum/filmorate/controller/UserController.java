package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping // получение списка всех пользователей
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}") // получение пользователя по ID
    public User getUserByID(@PathVariable Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой ID");
        }
        return userService.getUserByID(id);
    }

    @PostMapping // создание пользователя
    public User createUser(@RequestBody User user) {
        validateUser(user); // проверяем данные пользователя
        return userService.createUser(user);
    }

    @PutMapping // обновление данных о пользователе
    public User updateUser(@RequestBody User user) {
        validateUser(user); // проверяем данные пользователя
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}") // добавление в друзья
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}") // удаление из друзей
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends") // возвращаем список пользователей, являющихся его друзьями
    public List<User> getListOfFriends (@PathVariable Long userId) {
        return userService.getListOfFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}") // список друзей, общих с другим пользователем
    public Set<User> getListOfCommonFriends (@PathVariable Long userId, @PathVariable Long otherId) {
        return userService.getListOfCommonFriends(userId, otherId);
    }

    public void validateUser (User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и/или содержать пробелы!");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
