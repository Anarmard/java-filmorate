# Кинорейтинг (FilmoRate)

> Кинорейтинг - сервис, который помогает пользователю выбрать фильм для просмотра на основе оценок пользователей.   
> Пользователи могут просматривать ТОП-N фильмов с высоким рейтингом, рекомендованных для просмотра.
> В сервисе также можно посмотреть подробную информацию о фильме, которая поможет пользователю сделать правильный выбор.

## Оглавление
- [Функциональность приложения](#функциональность-приложения)
- [Схема БД и модели данных](#схема-бд-и-модели-данных)
- [Описание API](#описание-api)
- [Сборка и установка](#сборка-и-установка)
- [Стек технологий](#стек-технологий)

## Функциональность приложения
Приложение соответствует принципам REST API.
- Контроллеры (FilmController / UserController / GenreController / MpaController)
- Сервисы (FilmService / UserService / GenreService / MpaService)
- Хранилища (FilmStorage / UserStorage / GenreStorage / MpaStorage)

Приложение позволяет:
- Сохранять, находить и редактировать фильмы или пользователей
- Добавлять в друзья (или удалять из друзей) других пользователей, получать список друзей пользователя
- Ставить (или удалять) лайк фильму, находить ТОП фильмов с самым большим количеством лайков

## Схема БД и модели данных
Модели:
- **Film** (Фильм)
- **User** (Пользователь)
- **Mpa** (Рейтинг)
- **Genre** (Жанр)

Схема БД:
![Scheme of Filmorate database](/FILMORATE_diagram.png)

## Описание API
<details>
  <summary><h3>Фильм</h3></summary>
  
- **GET** /films - получение списка всех фильмов
- **GET** /films/{filmId} - получение фильма по ID
- **POST** /films - добавление фильма
- **PUT** /films - обновление данных о фильме
- **PUT** /films/{id}/like/{userId} - пользователь ставит лайк фильму
- **DELETE** /films/{id}/like/{userId} - пользователь удаляет лайк
- **GET** /films/popular?count={count} - возвращает список из первых N фильмов по количеству лайков
</details>
<details> 
 <summary><h3>Пользователь</h3></summary>
  
- **GET** /users - получение списка всех пользователей
- **GET** /users/{userId} - получение пользователя по ID
- **POST** /users - создание пользователя
- **PUT** /users - обновление данных о пользователе
- **PUT** /users/{userId}/friends/{friendId} - добавление в друзья
- **DELETE** /users/{userId}/friends/{friendId} - удаление из друзей
- **GET** /users/{userId}/friends - получение списка пользователей, являющихся его друзьями
- **GET** /users/{userId}/friends/common/{otherId} - получение списка друзей, общих с другим пользователем
</details>
<details> 
  <summary><h3>Рейтинг</h3></summary>

- **GET** /mpa - получение списка всех рейтингов
- **GET** /mpa/{id} - получение рейтинга по ID
</details>
<details>
  <summary><h3>Жанр</h3></summary>

- **GET** /genres - получение списка всех жанров
- **GET** /genres/{id} - получение жанра по ID
</details>

## Сборка и установка
Требования:
- Git
- JDK 11 или выше
- Maven 3.6.0 или выше

Как запустить приложение:
1. Склонируйте репозиторий на локальную компьютер:
```bash
https://github.com/Anarmard/java-filmorate.git
```
2. Перейдите в директорию проекта:
```bash
cd java-filmorate
```
3. Соберите проект:
```bash
mvn clean install
```
4. Запустите приложение:
```bash
mvn spring-boot:run
```

## Стек технологий
- Java 11
- Spring Boot (+ H2 Database)
- Apache Maven
- Lombok (+ SLF4J)
- JUnit 5
