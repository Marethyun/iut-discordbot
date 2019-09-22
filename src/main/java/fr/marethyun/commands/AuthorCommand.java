package fr.marethyun.commands;

import discord4j.core.object.entity.Message;
import fr.marethyun.CommandException;
import fr.marethyun.CommandsRegister;

public class AuthorCommand implements CommandsRegister.CommandExecutor {
    @Override
    public void execute(Message message, String mention) throws CommandException {
        message.getChannel().subscribe(channel -> channel.createMessage(String.format("%s, Ange Bacci a créé ce bot !", mention)).subscribe());
    }
}
