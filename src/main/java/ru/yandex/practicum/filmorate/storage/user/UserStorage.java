package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserStorage {
    // будут определены методы добавления, удаления и модификации объектов

    Collection<User> findAllUsers();

    User getUserByID(Long userId);

    User createUser(User user);

    User updateUser(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getListOfFriends(User user);

    Set<User> getListOfCommonFriends(User user, User friend);
}
