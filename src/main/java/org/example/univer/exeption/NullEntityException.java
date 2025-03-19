package org.example.univer.exeption;

public class NullEntityException extends ServiceException {
    private static final long serialVersionUID = 5767243652304305536L;

    public NullEntityException(String message) {
        super(message);
    }

    public NullEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}