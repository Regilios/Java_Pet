package org.example.univer.exeption;

public class SubjectExeption extends ServiceException {
    private static final long serialVersionUID = -3967225500384113898L;
    public SubjectExeption(String message) {
        super(message);
    }

    public SubjectExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
