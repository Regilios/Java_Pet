package org.example.univer.exeption;

public class StudentExeption extends ServiceException {
    private static final long serialVersionUID = 7167794410403977124L;

    public StudentExeption(String message) {
        super(message);
    }

    public StudentExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
