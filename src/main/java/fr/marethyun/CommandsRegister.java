package fr.marethyun;

import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandsRegister implements MessageProcessor {
    private char prefix;
    ArrayList<Command> commands = new ArrayList<>();

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

    public interface CommandExecutor {
        void execute(Message message) throws CommandException;
    }

    @Override
    public void processMessage(Message message) {
        if (!message.getContent().isPresent()) return;

        // If the command is prefixed with the configured prefix
        if (message.getContent().get().matches(String.format("^%s", this.prefix))) {
            // Retrieve the mention string, used later to mention the user

            // The call to get() is presumably safe, value presence has been checked previously
            assert message.getAuthor().isPresent();
            String mention = message.getAuthor().get().getMention();

            // splits the message and get rid of prefix
            String[] splittedMessage = message.getContent().get().substring(1).split(" ");

            // we assume that a command can be empty
            String commandRoot = splittedMessage.length < 1 ? "" : splittedMessage[0];

            List<Command> commands = this.commands.stream().
                    filter(command -> command.getAliases().contains(commandRoot))
                    .collect(Collectors.toList());

            if (commands.isEmpty()) {
                // The command does not exists
                message.getChannel().subscribe(messageChannel -> messageChannel.createMessage(String.format("%s, %s", mention, App.getInstance().getMessagesConfig().commandmissing)));

                return;
            }

            if (commands.size() > 1) throw new CommandException("Too much commands with the same alias..");

            // Get the selected command and execute it providing the message
            commands.get(0).execute(message);
        }
    }

    private class Command {
        private CommandExecutor executor;
        private ArrayList<String> aliases;

        public Command(CommandExecutor executor, ArrayList<String> aliases) {
            this.executor = executor;
            this.aliases = aliases;
        }

        public void execute(Message message) {
            this.executor.execute(message);
        }

        public CommandExecutor getExecutor() {
            return executor;
        }

        public ArrayList<String> getAliases() {
            return aliases;
        }
    }
}
