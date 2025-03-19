package org.example.univer.exeption;

public class LectureTimeExeption extends ServiceException {
    private static final long serialVersionUID = 3236902349861186129L;
    public LectureTimeExeption(String message) {
        super(message);
    }

    public LectureTimeExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
