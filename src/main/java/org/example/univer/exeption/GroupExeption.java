package org.example.univer.exeption;

public class GroupExeption extends ServiceException {
    private static final long serialVersionUID = -7269673103105341315L;
    public GroupExeption(String message) {
        super(message);
    }

    public GroupExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
