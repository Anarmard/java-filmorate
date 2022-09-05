package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // получение списка всех пользователей
    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    // получение пользователя по ID
    public Optional<User> getUserByID(Long id) {
        return userStorage.getUserByID(id);
    }

    // создание пользователя
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    // обновление данных о пользователе
    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    // добавление в друзья
    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    // удаление из друзей
    public boolean deleteFriend(Long userId, Long friendId) {
        return userStorage.deleteFriend(userId, friendId);
    }

    // возвращаем список пользователей, являющихся его друзьями
    public List<User> getListOfFriends(Long userId) {
        return userStorage.getListOfFriends(userId);
    }

    // список друзей, общих с другим пользователем
    public List<User> getListOfCommonFriends(Long userId, Long otherId) {
        return userStorage.getListOfCommonFriends(userId, otherId);
    }
}
