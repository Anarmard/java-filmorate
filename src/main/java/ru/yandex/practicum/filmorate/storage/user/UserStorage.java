package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    // будут определены методы добавления, удаления и модификации объектов

    Collection<User> findAllUsers();

    Optional<User> getUserByID(Long userId);

    User createUser(User user);

    Optional<User> updateUser(User user);

    void addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

    List<User> getListOfFriends(Long userId);

    List<User> getListOfCommonFriends(Long userId, Long friendId);
}
