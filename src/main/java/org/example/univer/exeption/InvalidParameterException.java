package org.example.univer.exeption;

public class InvalidParameterException extends ServiceException {
    private static final long serialVersionUID = -1272880253064092444L;

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
