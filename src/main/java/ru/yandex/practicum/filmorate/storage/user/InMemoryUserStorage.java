package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    //перенесите туда всю логику хранения, обновления и поиска объектов.

    private final Map<Long, User> userMap = new HashMap<>();
    private Long lastUsedId = 1L;

    private Long getNextId() {
        return (lastUsedId++);
    }

    // получение списка всех пользователей
    @Override
    public Collection<User> findAllUsers() {
        log.info("Количество пользователей: {}", userMap.size());
        return userMap.values();
    }

    // получение пользователя по ID
    @Override
    public User getUserByID(Long id) {
        User user = userMap.get(id);
        if (user == null) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
        return user;
    }

    // создание пользователя
    @Override
    public User createUser(User user) {
        user.setId(getNextId()); // присваиваем новый ID
        userMap.put(user.getId(), user);
        log.debug("Добавлен пользователь {}", user);
        return userMap.get(user.getId());
    }

    // обновление данных о пользователе
    @Override
    public User updateUser(User user) {
        if (!userMap.containsKey(user.getId())) {
            throw new NotFoundException("Указан неверный ID пользователя");
        }
        userMap.put(user.getId(), user);
        log.debug("Обновлены данные пользователя {}", user);
        return userMap.get(user.getId());
    }

    // добавление в друзья
    @Override
    public void addFriend(User user, User friend) {
        // Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
        // То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
        // В следующих спринтах логика поменяется, не забыть переделать
        user.getFriendsID().add(friend.getId());
        friend.getFriendsID().add(user.getId());
    }

    // удаление из друзей
    @Override
    public void deleteFriend(User user, User friend) {
        // Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
        // То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
        // В следующих спринтах логика поменяется, не забыть переделать
        user.getFriendsID().remove(friend.getId());
        friend.getFriendsID().remove(user.getId());
    }

    // возвращаем список пользователей, являющихся его друзьями
    @Override
    public List<User> getListOfFriends(User user) {
        List<User> listOfFriends = new ArrayList<>();
        for (Long id : user.getFriendsID()) {
            listOfFriends.add(getUserByID(id));
        }
        return listOfFriends;
    }

    // список друзей, общих с другим пользователем
    @Override
    public Set<User> getListOfCommonFriends(User user, User friend) {
        Set<Long> setIdOfCommonFriends = user.getFriendsID().stream()
                .filter(friend.getFriendsID()::contains).collect(Collectors.toSet());

        Set<User> setOfCommonFriends = new HashSet<>();

        for (Long id : setIdOfCommonFriends) {
            setOfCommonFriends.add(getUserByID(id));
        }
        return setOfCommonFriends;
    }
}