package tam.summer.database.exception;

/**
 * Created by tanqimin on 2016/1/20.
 */
public class ConfigException
        extends RuntimeException {
    public ConfigException() {
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
