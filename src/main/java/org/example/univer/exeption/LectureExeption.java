package org.example.univer.exeption;

public class LectureExeption extends ServiceException {
    private static final long serialVersionUID = -2891218479634897921L;
    public LectureExeption(String message) {
        super(message);
    }

    public LectureExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
