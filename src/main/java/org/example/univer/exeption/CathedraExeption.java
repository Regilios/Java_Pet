package org.example.univer.exeption;

public class CathedraExeption extends ServiceException{
    private static final long serialVersionUID = -4581914182076270343L;
    public CathedraExeption(String message) {
        super(message);
    }

    public CathedraExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
