package fr.marethyun.config;

/**
 * Represents the main configuration file
 */
public final class MainConfig {

    public static final String GROUPS_ATTRIBUTION_ENABLED = "enabled";

    public String discordtoken;
    public CommandsConfig commands;
    public String groupsattribution;

    public MainConfig() {
    }

    public MainConfig(String discordtoken, CommandsConfig commands, String groupsattribution) {
        this.discordtoken = discordtoken;
        this.commands = commands;
        this.groupsattribution = groupsattribution;
    }

    public String getDiscordtoken() {
        return discordtoken;
    }

    public void setDiscordtoken(String discordtoken) {
        this.discordtoken = discordtoken;
    }

    public String getGroupsattribution() {
        return groupsattribution;
    }

    public void setGroupsattribution(String groupsattribution) {
        this.groupsattribution = groupsattribution;
    }

    public CommandsConfig getCommands() {
        return commands;
    }

    public void setCommands(CommandsConfig commands) {
        this.commands = commands;
    }


}
