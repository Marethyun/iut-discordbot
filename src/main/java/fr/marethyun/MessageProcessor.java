package fr.marethyun;

import discord4j.core.object.entity.Message;

/**
 * Just provides a method to process a discord message
 *
 * @see CommandsRegister
 * @see GroupAllocator
 */
public interface MessageProcessor {
    /**
     * Processes a message
     *
     * @param message The message
     */
    void processMessage(Message message);
}
