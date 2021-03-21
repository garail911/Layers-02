package tech.itpark.exception;

public class PasswordInvalidException extends RuntimeException {

    public PasswordInvalidException() {
        super();
    }

    public PasswordInvalidException(String message) {
        super(message);
    }

    public PasswordInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordInvalidException(Throwable cause) {
        super(cause);
    }

    protected PasswordInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
