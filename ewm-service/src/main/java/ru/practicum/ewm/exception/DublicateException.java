package ru.practicum.ewm.exception;

public class DublicateException extends RuntimeException{
    public DublicateException(String message) {
        super(message);
    }
}
