package ru.evernight.exception;


import javax.ejb.ApplicationException;

@ApplicationException
public class EvernightException extends Exception {
    public EvernightException() {
    }

    public EvernightException(String message) {
        super(message);
    }

    public EvernightException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvernightException(Throwable cause) {
        super(cause);
    }
}
