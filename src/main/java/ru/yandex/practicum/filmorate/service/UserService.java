package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// Отвечать за такие операции с пользователями, как добавление в друзья, удаление из друзей, вывод списка общих друзей.
// Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
// То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.

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
    public User getUserByID(Long id) {
        return userStorage.getUserByID(id);
    }

    // создание пользователя
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    // обновление данных о пользователе
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    // добавление в друзья
    public void addFriend(Long userId, Long friendId) {
        // передаем не ID, а самого пользователя. Совет наставника, на будущее при работе с БД
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        userStorage.addFriend(user, friend);
    }

    // удаление из друзей
    public void deleteFriend(Long userId, Long friendId) {
        // передаем не ID, а самого пользователя. Совет наставника, на будущее при работе с БД
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(friendId);
        userStorage.deleteFriend(user, friend);
    }

    // возвращаем список пользователей, являющихся его друзьями
    public List<User> getListOfFriends(Long userId) {
        // передаем не ID, а самого пользователя. Совет наставника, на будущее при работе с БД
        User user = userStorage.getUserByID(userId);
        return userStorage.getListOfFriends(user);
    }

    // список друзей, общих с другим пользователем
    public Set<User> getListOfCommonFriends(Long userId, Long otherId) {
        // передаем не ID, а самого пользователя. Совет наставника, на будущее при работе с БД
        User user = userStorage.getUserByID(userId);
        User friend = userStorage.getUserByID(otherId);
        return userStorage.getListOfCommonFriends(user, friend);
    }
}
