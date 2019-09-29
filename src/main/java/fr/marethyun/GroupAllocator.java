package fr.marethyun;

import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import fr.marethyun.config.GroupType;
import fr.marethyun.config.GroupsAllocationConfig;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Singleton class, responsible of the group allocation system.
 */
public final class GroupAllocator implements MessageProcessor {

    /**
     * The class' logger
     */
    private static final Logger LOGGER = Logger.getLogger(GroupAllocator.class.getName());

    /**
     * The singleton instance, defaultly null
     */
    private static GroupAllocator instance = null;

    /**
     * The csv format containing the right header specified in the configuration file
     */
    private final CSVFormat csvFormat;

    /**
     * List of the CSV header fields, as strings
     */
    private final List<String> fields;

    /**
     * The singleton instance getter
     * @return a new instance if {@link GroupAllocator#instance} is null, {@link GroupAllocator#instance} otherwise
     */
    public static GroupAllocator getInstance() {
        return instance == null ? new GroupAllocator() : instance;
    }

    /**
     * The class' constructor.
     *
     * Builds the header and the fields using the configuration.
     *
     * @throws BotException if there were a problem loading the files
     */
    private GroupAllocator() throws BotException {
        String[] splittedFields = App.getInstance().getGroupsAllocationConfig().rawdatapattern.split(";");
        this.csvFormat = CSVFormat.RFC4180.withHeader(splittedFields);
        this.fields = Arrays.asList(splittedFields);

        GroupsAllocationConfig config = App.getInstance().getGroupsAllocationConfig();

        // if the pattern and the specified fields does not match
        if (!this.fields.contains(config.matchfield) || !this.fields.contains(config.groupfield)) {
            throw new BotException("Some specified fields does not match with the provided pattern");
        }
    }

    /**
     * Processes the private message.
     *
     * @param message The message
     */
    @Override
    public void processMessage(Message message) {
        // The message author MUST have been checked before
        assert message.getAuthor().isPresent();

        User author = message.getAuthor().get();

        if (!message.getContent().isPresent()) {
            // the bot cannot understand
            message.getChannel().subscribe(channel -> channel.createMessage(App.getInstance().getMessagesConfig().misunderstandmessage));
        }

        String csvFilePath = App.getInstance().getCsvPath();

        Iterable<CSVRecord> records;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(csvFilePath)))) {
            records = this.csvFormat.parse(reader);

            for (CSVRecord next : records) {
                if (next.get(App.getInstance().getGroupsAllocationConfig().matchfield).equals(message.getContent().get())) {
                    // if the message content matches the 'check' field

                    String groupFieldContent = next.get(App.getInstance().getGroupsAllocationConfig().groupfield);

                    List<GroupType> types = App.getInstance().getGroupsAllocationConfig().grouptypes.stream()
                            .filter(groupType -> groupType.name.equals(groupFieldContent))
                            .collect(Collectors.toList());

                    // If there is not group type matching the found type
                    if (types.isEmpty()) {
                        message.getChannel().subscribe(channel -> channel.createMessage(App.getInstance().getMessagesConfig().allocationerror).subscribe());
                        LOGGER.warning(String.format("There is no group matching the found type '%s'", groupFieldContent));
                        return;
                    }

                    GroupType type = types.get(0);

                    DiscordClient client = App.getInstance().getDiscordClient();

                    Snowflake guildId = Snowflake.of(App.getInstance().getGroupsAllocationConfig().guildid);
                    Snowflake roleId = Snowflake.of(type.id);
                    Mono<Member> member = client.getMemberById(guildId, author.getId());

                    member.subscribe(m -> {

                        // Build a map with the fields and their content
                        HashMap<String, String> filledFields = new HashMap<>();
                        for (String field : this.fields) {
                            filledFields.put(field, next.get(field));
                        }
                        filledFields.put("GROUP_DESCRIPTION", type.description);

                        if (m.getRoleIds().contains(roleId)) {
                            // L'utilisateur a déjà son groupe
                            message.getChannel().subscribe(channel -> {
                                channel.createMessage(patchMessage(filledFields, App.getInstance().getMessagesConfig().alreadyallocated)).subscribe();
                            });

                            LOGGER.info(String.format("The user {%s} (%s) requested his role but already have his.", author.getId().asString(), author.getUsername()));
                        } else {
                            // Le groupe est attribué
                            m.addRole(roleId).subscribe();

                            message.getChannel().subscribe(channel -> {
                                // Send the field-patched message back to the user to confirm the group allocation
                                channel.createMessage(patchMessage(filledFields, App.getInstance().getMessagesConfig().allocationmessage)).subscribe();
                            });
                            LOGGER.info(String.format("Successfully allocated role '%s' (%s) to {%s} (%s)", roleId.asString(), type.name, author.getId().asString(), author.getUsername()));
                        }
                    });
                    return;
                }
            }
            // Si on arrive là, c'est qu'aucune entrée ne correspond
            message.getChannel().subscribe(channel -> {
                channel.createMessage(App.getInstance().getMessagesConfig().nodatamatch).subscribe();
            });

            LOGGER.warning(String.format("User {%s} (%s) gave the bot something he could not process.", author.getId().asString(), author.getUsername()));

        } catch (Exception e) {
            LOGGER.severe("A problem occured while trying to allocate a group.");
            throw new BotException("An exception occured while reading the csv data file..", e);
        }
    }

    /**
     * Patched the message by replacing fields placeholders by actual read values of the fields.
     *
     * @param fields The fields with their values
     * @param message The message to patch, as a string
     * @return The patched message
     */
    private String patchMessage(HashMap<String, String> fields, String message) {

        String result = message;

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            result = result.replace(String.format("{%s}", entry.getKey()), entry.getValue());
        }

        return result;
    }

}
