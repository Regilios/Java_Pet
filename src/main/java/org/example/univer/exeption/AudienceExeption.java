package org.example.univer.exeption;

public class AudienceExeption extends ServiceException {
    private static final long serialVersionUID = -5695200996437065897L;
    public AudienceExeption(String message) {
        super(message);
    }
    public AudienceExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
