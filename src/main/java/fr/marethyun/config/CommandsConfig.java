package fr.marethyun.config;

public final class CommandsConfig {
    public String guildid;
    public String channelid;
    public char prefix;

    public CommandsConfig() {
    }

    public CommandsConfig(String guildid, String channelid, char prefix) {
        this.guildid = guildid;
        this.channelid = channelid;
        this.prefix = prefix;
    }

    public String getGuildid() {
        return guildid;
    }

    public void setGuildid(String guildid) {
        this.guildid = guildid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public char getPrefix() {
        return prefix;
    }

    public void setPrefix(char prefix) {
        this.prefix = prefix;
    }
}