package fr.marethyun.commands;

import discord4j.core.object.entity.Message;
import fr.marethyun.App;
import fr.marethyun.CommandException;
import fr.marethyun.CommandsRegister;

public class HelpCommand implements CommandsRegister.CommandExecutor {
    /**
     * Sends the user a list of the registered command with their first alias.
     *
     * @param message The discord message
     * @param mention The sender's mention string
     */
    @Override
    public void execute(Message message, String mention) throws CommandException {
        message.getChannel().subscribe(channel -> {

            String result = String.format("%s, les commandes disponibles sont: \n", mention);
            for (CommandsRegister.Command command : App.getInstance().getCommandsRegister().getCommands()) {
                result += String.format("> %s%s\n", App.getInstance().getCommandsRegister().getPrefix(), command.getAliases().get(0));
            }

            channel.createMessage(result).subscribe();
        });
    }
}
