package fr.marethyun;

import discord4j.core.object.entity.Message;

public interface MessageProcessor {
    void processMessage(Message message);
}
