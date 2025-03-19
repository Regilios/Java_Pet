package org.example.univer.exeption;

public class EntityNotFoundException extends ServiceException {
    private static final long serialVersionUID = 6430887944578008277L;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
