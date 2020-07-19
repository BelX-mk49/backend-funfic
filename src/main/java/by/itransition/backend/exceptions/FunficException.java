package by.itransition.backend.exceptions;

public class FunficException extends RuntimeException {
    public FunficException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public FunficException(String exMessage) {
        super(exMessage);
    }
}
