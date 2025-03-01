package org.example.univer.exeption;

public class VacationExeption extends ServiceException {
    private static final long serialVersionUID = -4169102691045240250L;
    public VacationExeption(String message) {
        super(message);
    }

    public VacationExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
