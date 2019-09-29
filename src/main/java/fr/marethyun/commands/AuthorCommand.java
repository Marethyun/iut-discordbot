package fr.marethyun.commands;

import discord4j.core.object.entity.Message;
import fr.marethyun.CommandException;
import fr.marethyun.CommandsRegister;

public class AuthorCommand implements CommandsRegister.CommandExecutor {
    /**
     * Sends the user the author of the bot (currently Ange Bacci)
     *
     * @param message The discord message
     * @param mention The sender's mention string
     */
    @Override
    public void execute(Message message, String mention) throws CommandException {
        message.getChannel().subscribe(channel -> channel.createMessage(String.format("%s, Ange Bacci a créé ce bot !", mention)).subscribe());
    }
}
