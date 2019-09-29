/*
 *    Copyright 2019 Ange Bacci
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
