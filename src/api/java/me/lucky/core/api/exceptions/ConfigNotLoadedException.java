package me.lucky.core.api.exceptions;

public class ConfigNotLoadedException extends Exception {
    public ConfigNotLoadedException() {
    }

    public ConfigNotLoadedException(String message) {
        super(message);
    }

    public ConfigNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigNotLoadedException(Throwable cause) {
        super(cause);
    }

    public ConfigNotLoadedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
