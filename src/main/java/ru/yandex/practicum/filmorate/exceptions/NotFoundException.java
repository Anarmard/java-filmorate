package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String s) {
        super(s);
    }
}
