package fr.marethyun;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import fr.marethyun.config.GroupsAllocationConfig;
import fr.marethyun.config.MainConfig;
import fr.marethyun.config.MessagesConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class App {

    private static App instance;

    private MainConfig mainConfig;
    private MessagesConfig messagesConfig;
    private GroupsAllocationConfig groupsAllocationConfig;
    private CommandsRegister commandsRegister;

    public static App getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance = new App("config.yaml", "groupsallocation.yaml", "messages.yaml");
    }

    public App(String mainConfigPath, String groupsConfigPath, String messagesConfigPath) {

        this.mainConfig = loadConfig(MainConfig.class, mainConfigPath);
        this.messagesConfig = loadConfig(MessagesConfig.class, messagesConfigPath);

        // If the groups attribution system is enabled
        if (this.mainConfig.groupsattribution.equalsIgnoreCase(MainConfig.GROUPS_ATTRIBUTION_ENABLED)) {
            this.groupsAllocationConfig = loadConfig(GroupsAllocationConfig.class, groupsConfigPath);
        }

        commandsRegister = new CommandsRegister(mainConfig.commands.prefix);

        DiscordClient client = new DiscordClientBuilder(mainConfig.discordtoken).build();

        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {

            // Abort the operation if the message is issued outside a guild
            if (!messageCreateEvent.getGuildId().isPresent()) return;

            // If the channelId and the guildId matchs with the configuration
            if (messageCreateEvent.getMessage().getChannelId().asString().equals(App.this.mainConfig.commands.channelid) && messageCreateEvent.getGuildId().get().asString().equals(App.this.mainConfig.commands.guildid)) {
                // Deal with the supposed command message
                App.this.commandsRegister.processMessage(messageCreateEvent.getMessage());
            }
        });

        // If the groups attribution system is enabled
        if (this.mainConfig.groupsattribution.equalsIgnoreCase(MainConfig.GROUPS_ATTRIBUTION_ENABLED)) {
            // Register the event corresponding to group attribution queries
            client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {
                User user = messageCreateEvent.getMessage().getAuthor().orElseThrow(() -> new BotException("Wtf this message has no author"));

                user.getPrivateChannel().subscribe(channel -> {
                    if (channel.getId().equals(messageCreateEvent.getMessage().getChannelId())) {
                        // Deal with the query
                    }
                });
            });
        }

        client.login().block();

    }

    private <T> T loadConfig(Class<T> configClass, String filePath) {
        Yaml yaml = new Yaml(new Constructor(configClass));

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            return yaml.load(reader);
        } catch (IOException e) {
            throw new BotException("An I/O error ocurred while loading a configuration file.", e);
        }
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public GroupsAllocationConfig getGroupsAllocationConfig() {
        return groupsAllocationConfig;
    }
}
