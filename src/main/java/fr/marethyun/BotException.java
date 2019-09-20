package fr.marethyun;

public class BotException extends RuntimeException {
    public BotException() {}

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }
}
