package fr.marethyun;

/**
 * An exception matching the bot errors
 */
public class BotException extends RuntimeException {
    public BotException() {}

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }
}
