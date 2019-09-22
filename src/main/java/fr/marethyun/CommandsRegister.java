package fr.marethyun;

import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class CommandsRegister implements MessageProcessor {

    private static final Logger LOGGER = Logger.getLogger(CommandsRegister.class.getName());

    private char prefix;
    private ArrayList<Command> commands = new ArrayList<>();

    public CommandsRegister(char prefix) {
        this.prefix = prefix;
    }

    public void registerCommand(CommandExecutor executor, String... aliases) {
        if (aliases.length < 1) throw new CommandException("A command must at least have one alias");
        this.commands.add(new Command(executor, new ArrayList<>(Arrays.asList(aliases))));
    }

    public void registerCommand(CommandExecutor executor, String alias) {
        registerCommand(executor, new String[]{alias});
    }

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

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public char getPrefix() {
        return prefix;
    }

    public interface CommandExecutor {
        void execute(Message message, String mention) throws CommandException;
    }

    public class Command {
        private CommandExecutor executor;
        private ArrayList<String> aliases;

        public Command(CommandExecutor executor, ArrayList<String> aliases) {
            this.executor = executor;
            this.aliases = aliases;
        }

        public void execute(Message message, String mention) {
            this.executor.execute(message, mention);
        }

        public CommandExecutor getExecutor() {
            return executor;
        }

        public ArrayList<String> getAliases() {
            return aliases;
        }
    }
}
