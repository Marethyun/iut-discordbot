package fr.marethyun;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import fr.marethyun.commands.AuthorCommand;
import fr.marethyun.commands.HelpCommand;
import fr.marethyun.config.GroupsAllocationConfig;
import fr.marethyun.config.MainConfig;
import fr.marethyun.config.MessagesConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * @author Ange Bacci <ange.bacci@etu.univ-amu.fr>
 *
 * Main class, contains the application entry point and act as a junction between the
 * bot's components.
 */
public class App {

    /**
     * The class' logger
     */
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    /**
     * The singleton instance
     */
    private static App instance;

    /**
     * The path of the CSV file used to fetch groups data
     */
    private String csvPath;

    /**
     * The configuration entity for the main configuration file
     */
    private MainConfig mainConfig;

    /**
     * The configuration entity for the messages configuration file
     */
    private MessagesConfig messagesConfig;

    /**
     * The configuration entity for the groups configuration file
     */
    private GroupsAllocationConfig groupsAllocationConfig;

    /**
     * The bot's commands register
     */
    private CommandsRegister commandsRegister;

    /**
     * The discord API client instance
     */
    private DiscordClient discordClient;

    /**
     * @return The singleton instance
     */
    public static App getInstance() {
        return instance;
    }

    /**
     * The main method.
     *
     * Checks if the provided files exists and if one of them is missing create
     * it and abord the bot initialization.
     * This prevents a crash when the first configuration file is generated
     *
     * @param args The command line arguments
     */
    // args[0] - chemin vers le fichier CSV contenant les entrées (défaut: data.csv)
    // args[1] - chemin vers le fichier de configuration principal (défaut: config.yaml)
    // args[2] - chemin vers le fichier de configuration des groupes (défaut: groups.yaml)
    // args[3] - chemin vers le fichier de configuration des messages (défaut: messages.yaml)
    public static void main(String[] args) {

        String csvPath = args.length < 1 ? "data.csv" : args[0];
        String mainConfigPath = args.length < 2 ? "config.yaml" : args[1];
        String groupsConfigPath = args.length < 3 ? "groups.yaml" : args[2];
        String messagesConfigPath = args.length < 4 ? "messages.yaml" : args[3];

        try {

            if (!filesExist(csvPath, mainConfigPath, groupsConfigPath, messagesConfigPath)) {

                LOGGER.warning("Some files does not exist, creating them..");

                generateDefaultFile(csvPath, "/data.csv");
                generateDefaultFile(mainConfigPath, "/config.yaml");
                generateDefaultFile(groupsConfigPath, "/groups.yaml");
                generateDefaultFile(messagesConfigPath, "/messages.yaml");

                LOGGER.warning("Remember to change default settings.");
                LOGGER.warning("Exiting...");

                return;
            }
            new App(csvPath, mainConfigPath, groupsConfigPath, messagesConfigPath);
        } catch (Exception e) {
            LOGGER.severe("Something went wrong while initializing the bot.");
            e.printStackTrace();
        }
    }

    /**
     * The Application constructor
     *
     * @param csvPath Path to the CSV file
     * @param mainConfigPath Path to the main configuration file
     * @param groupsConfigPath Path to the groups configuration file
     * @param messagesConfigPath Path to the messages configuration file
     */
    private App(String csvPath, String mainConfigPath, String groupsConfigPath, String messagesConfigPath) {

        this.csvPath = csvPath;
        this.mainConfig = loadConfig(MainConfig.class, mainConfigPath);
        this.messagesConfig = loadConfig(MessagesConfig.class, messagesConfigPath);

        // If the groups attribution system is enabled
        if (this.mainConfig.groupsattribution.equalsIgnoreCase(MainConfig.GROUPS_ATTRIBUTION_ENABLED)) {
            this.groupsAllocationConfig = loadConfig(GroupsAllocationConfig.class, groupsConfigPath);
        }

        this.commandsRegister = new CommandsRegister(mainConfig.commands.prefix);

        // Add commands
        this.commandsRegister.registerCommand(new AuthorCommand(), "author");
        this.commandsRegister.registerCommand(new HelpCommand(), "help");

        // Build the API client
        this.discordClient = new DiscordClientBuilder(mainConfig.discordtoken).build();

        // Register the commands event
        this.discordClient.getEventDispatcher().on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {

            // Abort the operation if the message is issued outside a guild
            if (!messageCreateEvent.getGuildId().isPresent()) return;

            // If the message has no author
            if (!messageCreateEvent.getMessage().getAuthor().isPresent()) {
                throw new BotException("Encountered a message with no user ?!");
            }

            // If the bot has no self-id (i.e. it may not have been initialized properly)
            if (!App.this.discordClient.getSelfId().isPresent()) {
                throw new BotException("The bot has no self-id ?!");
            }

            // Ignore the messages coming from the bot itself
            if (App.this.discordClient.getSelfId().get().equals(messageCreateEvent.getMessage().getAuthor().get().getId())) return;

            // If the channelId and the guildId matchs with the configuration
            if (messageCreateEvent.getMessage().getChannelId().asString().equals(App.this.mainConfig.commands.channelid) && messageCreateEvent.getGuildId().get().asString().equals(App.this.mainConfig.commands.guildid)) {
                // Deal with the supposed command message
                App.this.commandsRegister.processMessage(messageCreateEvent.getMessage());
            }
        });

        // If the groups attribution system is enabled
        if (this.mainConfig.groupsattribution.equalsIgnoreCase(MainConfig.GROUPS_ATTRIBUTION_ENABLED)) {
            // Register the event corresponding to group attribution queries
            this.discordClient.getEventDispatcher().on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {
                User user = messageCreateEvent.getMessage().getAuthor().orElseThrow(() -> new BotException("Wtf this message has no author"));

                // Ignore the messages coming from the bot itself
                if (App.this.discordClient.getSelfId().get().equals(user.getId())) return;

                user.getPrivateChannel().subscribe(channel -> {
                    // If we are in the DM channel with the bot
                    if (channel.getId().equals(messageCreateEvent.getMessage().getChannelId())) {
                        // Deal with the query
                        GroupAllocator.getInstance().processMessage(messageCreateEvent.getMessage());
                    }
                });
            });
        }

        LOGGER.info("Bot assets initialized.");

        instance = this;
        this.discordClient.login().block();
    }

    /**
     * Checks if a file in the provided paths doesn't exists
     *
     * @param paths The file pathes
     * @return true if all the files exists, false otherwise
     */
    private static boolean filesExist(String... paths) {
        for (String path : paths) {
            if (!new File(path).exists()) return false;
        }
        return true;
    }

    /**
     * Generate a default file using a ressource in the classpath.
     *
     * @param configPath The file to write
     * @param resourcePath The resource containing the default file's content
     * @throws IOException If something went wront while accesing a file
     */
    private static void generateDefaultFile(String configPath, String resourcePath) throws IOException {

        File file = new File(configPath);
        // if the file already exists, exit
        if (file.exists()) return;

        file.createNewFile();

        BufferedReader reader = new BufferedReader(new InputStreamReader(App.class.getResourceAsStream(resourcePath), StandardCharsets.UTF_8));

//        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        FileWriter writer = new FileWriter(file);

        int b;
        while ((b = reader.read()) != -1) {
            writer.write(b);
        }

        writer.close();

    }

    /**
     * Loads a config file using SnakeYAML
     *
     * @param configClass The entity class to fill with YAML properties
     * @param filePath The file path
     * @param <T> The type of the entity class
     * @return The entity
     */
    private <T> T loadConfig(Class<T> configClass, String filePath) {
        Yaml yaml = new Yaml(new Constructor(configClass));

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            return yaml.load(reader);
        } catch (IOException e) {
            throw new BotException("An I/O error ocurred while loading a configuration file.", e);
        }
    }

    /**
     * Getter for the CSV Path
     * @return the CSV Path
     */
    public String getCsvPath() {
        return csvPath;
    }

    /**
     * Getter for the main configuration entity
     * @return the Main Config entity
     */
    public MainConfig getMainConfig() {
        return mainConfig;
    }

    /**
     * Getter for the messages configuration entity
     * @return the messages configuration entity
     */
    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    /**
     * Getter for the groups configuration entity
     * @return the groups configuration entity
     */
    public GroupsAllocationConfig getGroupsAllocationConfig() {
        return groupsAllocationConfig;
    }

    /**
     * Getter for the commands register
     * @return returns the commands register
     */
    public CommandsRegister getCommandsRegister() {
        return commandsRegister;
    }

    /**
     * Getter for the discord API client
     * @return the discord API client
     */
    public DiscordClient getDiscordClient() {
        return discordClient;
    }
}
