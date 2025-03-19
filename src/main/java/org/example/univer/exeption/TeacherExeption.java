package org.example.univer.exeption;

public class TeacherExeption extends ServiceException {
    private static final long serialVersionUID = -2801269161054701370L;
    public TeacherExeption(String message) {
        super(message);
    }

    public TeacherExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
