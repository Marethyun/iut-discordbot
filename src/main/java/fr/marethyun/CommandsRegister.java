package fr.marethyun;

import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The commands register
 *
 * This class is responsible for the handling of commands fired by the user in the provided channel and guild.
 */
public final class CommandsRegister implements MessageProcessor {

    /**
     * The class' logger
     */
    private static final Logger LOGGER = Logger.getLogger(CommandsRegister.class.getName());

    /**
     * The commands prefix
     */
    private char prefix;

    /**
     * The set the built command objects
     */
    private ArrayList<Command> commands = new ArrayList<>();

    /**
     * The class' constructor
     *
     * @param prefix The commands prefix
     */
    public CommandsRegister(char prefix) {
        this.prefix = prefix;
    }

    /**
     * Registers a command from a callback and aliases.
     *
     * A command can be referenced by multiple aliases.
     *
     * @param executor The callback
     * @param aliases The command aliases
     */
    public void registerCommand(CommandExecutor executor, String... aliases) {
        if (aliases.length < 1) throw new CommandException("A command must at least have one alias");
        this.commands.add(new Command(executor, new ArrayList<>(Arrays.asList(aliases))));
    }

    /**
     * Registers a command with only one alias, its name.
     *
     * @see CommandsRegister#registerCommand(CommandExecutor, String...)
     *
     * @param executor The callback
     * @param alias The command alias
     */
    public void registerCommand(CommandExecutor executor, String alias) {
        registerCommand(executor, new String[]{alias});
    }

    /**
     * Process a command from a discord message
     *
     * @param message The message
     */
    @Override
    public void processMessage(Message message) {

        String content = message.getContent().get();

        if (!message.getContent().isPresent()) return;
        if (content.isEmpty()) return;

        char commandPrefix = content.charAt(0);

        // If the command is prefixed with the configured prefix
        if (commandPrefix == this.prefix) {
            // Retrieve the mention string, used later to mention the user

            // The call to get() is presumably safe, value presence has been checked previously
            assert message.getAuthor().isPresent();
            String mention = message.getAuthor().get().getMention();

            // splits the message and get rid of prefix
            String[] splittedMessage = content.substring(1).split(" ");

            // we assume that a command can be empty
            String commandRoot = splittedMessage.length < 1 ? "" : splittedMessage[0];

            List<Command> commands = this.commands.stream().
                    filter(command -> command.getAliases().contains(commandRoot))
                    .collect(Collectors.toList());

            if (commands.isEmpty()) {
                // The command does not exists
                message.getChannel().subscribe(messageChannel -> messageChannel.createMessage(String.format("%s, %s", mention, App.getInstance().getMessagesConfig().commandmissing)).subscribe());
                LOGGER.warning(String.format("User {%s} (%s) issued a command that does not exists (%s).", message.getAuthor().get().getId().asString(), message.getAuthor().get().getUsername(), commandRoot));
                return;
            }

            if (commands.size() > 1) throw new CommandException("Too much commands with the same alias..");

            // Get the selected command and execute it providing the message
            commands.get(0).execute(message, mention);
            LOGGER.info(String.format("User {%s} (%s) issued command %s.", message.getAuthor().get().getId().asString(), message.getAuthor().get().getUsername(), commandRoot));
        }
    }

    /**
     * Getter for the commands
     * @return the commands
     */
    public ArrayList<Command> getCommands() {
        return commands;
    }

    /**
     * Getter for the prefix
     * @return the prefix
     */
    public char getPrefix() {
        return prefix;
    }

    /**
     * The callback interface.
     */
    public interface CommandExecutor {
        /**
         * Executes the command
         *
         * @param message The discord message
         * @param mention The sender's mention string
         * @throws CommandException if something went wrong
         */
        void execute(Message message, String mention) throws CommandException;
    }

    /**
     * A POJO that represents a command.
     *
     * Wraps a callback and aliases of the command.
     */
    public class Command implements CommandExecutor {
        /**
         * The callback
         */
        private CommandExecutor executor;

        /**
         * The aliases
         */
        private ArrayList<String> aliases;

        /**
         * The constructor
         *
         * @param executor The callback
         * @param aliases The aliases
         */
        public Command(CommandExecutor executor, ArrayList<String> aliases) {
            this.executor = executor;
            this.aliases = aliases;
        }

        /**
         * Just executes the wrapped callback
         *
         * @param message The discord message
         * @param mention The sender's mention string
         */
        @Override
        public void execute(Message message, String mention) {
            this.executor.execute(message, mention);
        }

        /**
         * The getter for the callback
         *
         * @return the callback
         */
        public CommandExecutor getExecutor() {
            return executor;
        }

        /**
         * The getter for the aliases
         *
         * @return the aliases
         */
        public ArrayList<String> getAliases() {
            return aliases;
        }
    }
}
