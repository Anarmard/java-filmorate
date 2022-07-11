package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static UserController userController = new UserController();
    User user = new User(1L, "mar_anar_az@mail.ru", "login1", "loginName",
            LocalDate.of(1988, 8,1));

    @BeforeEach
    void userCreate() {
        user.setId(1L);
        user.setEmail("mar_anar_az@mail.ru");
        user.setLogin("login1");
        user.setName("loginName");
        user.setBirthday(LocalDate.of(1988, 8,1));
    }

    @Test
    void emailIsNull() {
        user.setEmail(null);
        Exception exception = assertThrows(ValidationException.class, ()-> userController.validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", exception.getMessage());
    }

    @Test
    void emailWithOutAt() {
        user.setEmail("mar_anar_az.mail.ru");
        Exception exception = assertThrows(ValidationException.class, ()-> userController.validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", exception.getMessage());
    }

    @Test
    void loginIsNull() {
        user.setLogin(null);
        Exception exception = assertThrows(ValidationException.class, ()-> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и/или содержать пробелы!", exception.getMessage());
    }

    @Test
    void loginWithSpace() {
        user.setEmail("logi n1");
        Exception exception = assertThrows(ValidationException.class, ()-> userController.validateUser(user));
        assertEquals("Логин не может быть пустым и/или содержать пробелы!", exception.getMessage());
    }

    @Test
    void birthdayInFuturePlusOneDay() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Exception exception = assertThrows(ValidationException.class, ()-> userController.validateUser(user));
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
    }
}
