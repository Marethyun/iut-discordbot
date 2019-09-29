package fr.marethyun;

/**
 * Exception inherited from BotException
 *
 * @see BotException
 */
public class CommandException extends BotException {
    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
