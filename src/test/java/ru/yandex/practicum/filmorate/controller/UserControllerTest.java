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
    void emailIsNullCreate() {
        user.setEmail(null);
        Exception exception = assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", exception.getMessage());
    }

    @Test
    void emailIsNullUpdate() {
        user.setEmail(null);
        Exception exception = assertThrows(ValidationException.class, ()-> userController.updateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", exception.getMessage());
    }

    @Test
    void emailWithOutAtCreate() {
        user.setEmail("mar_anar_az.mail.ru");
        Exception exception = assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", exception.getMessage());
    }

    @Test
    void emailWithOutAtUpdate() {
        user.setEmail("mar_anar_az.mail.ru");
        Exception exception = assertThrows(ValidationException.class, ()-> userController.updateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @!", exception.getMessage());
    }

    @Test
    void loginIsNullCreate() {
        user.setLogin(null);
        Exception exception = assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("Логин не может быть пустым и/или содержать пробелы!", exception.getMessage());
    }

    @Test
    void loginIsNullUpdate() {
        user.setLogin(null);
        Exception exception = assertThrows(ValidationException.class, ()-> userController.updateUser(user));
        assertEquals("Логин не может быть пустым и/или содержать пробелы!", exception.getMessage());
    }

    @Test
    void loginWithSpaceCreate() {
        user.setLogin("logi n1");
        Exception exception = assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("Логин не может быть пустым и/или содержать пробелы!", exception.getMessage());
    }

    @Test
    void loginWithSpaceUpdate() {
        user.setLogin("logi n1");
        Exception exception = assertThrows(ValidationException.class, ()-> userController.updateUser(user));
        assertEquals("Логин не может быть пустым и/или содержать пробелы!", exception.getMessage());
    }

    @Test
    void birthdayInFuturePlusOneDayCreate() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Exception exception = assertThrows(ValidationException.class, ()-> userController.createUser(user));
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
    }

    @Test
    void birthdayInFuturePlusOneDayUpdate() {
        user.setBirthday(LocalDate.now().plusDays(1));
        Exception exception = assertThrows(ValidationException.class, ()-> userController.updateUser(user));
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
    }
}
