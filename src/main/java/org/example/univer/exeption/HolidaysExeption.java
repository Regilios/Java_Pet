package org.example.univer.exeption;

public class HolidaysExeption extends ServiceException {
    private static final long serialVersionUID = 4470251809931760685L;

    public HolidaysExeption(String message) {
        super(message);
    }

    public HolidaysExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
