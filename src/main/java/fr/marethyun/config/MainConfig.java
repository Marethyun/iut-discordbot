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
