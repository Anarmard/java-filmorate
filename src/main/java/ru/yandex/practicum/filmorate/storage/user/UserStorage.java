package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    // будут определены методы добавления, удаления и модификации объектов
    public Long getNextId();

    public Collection<User> findAllUsers();

    public User getUserByID(Long userId);

    public User createUser(User user);

    public User updateUser(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getListOfFriends(User user);

    List<User> getListOfCommonFriends(User user, User friend);
}
