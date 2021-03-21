package tech.itpark.exception;

public class UsernameNotExistsException extends RuntimeException {


    public UsernameNotExistsException() {
        super();
    }

    public UsernameNotExistsException(String message) {
        super(message);
    }

    public UsernameNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameNotExistsException(Throwable cause) {
        super(cause);
    }

    protected UsernameNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
