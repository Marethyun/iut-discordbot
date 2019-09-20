package fr.marethyun;

import discord4j.core.object.entity.Message;
import fr.marethyun.config.GroupType;
import fr.marethyun.config.GroupsAllocationConfig;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public final class GroupAllocator implements MessageProcessor {
    private static GroupAllocator instance = null;

    private final CSVFormat csvFormat;
    private final List<String> fields;

    public static GroupAllocator getInstance() {
        return instance == null ? new GroupAllocator() : instance;
    }

    private GroupAllocator() {
        String[] splittedFields = App.getInstance().getGroupsAllocationConfig().datafile.rawdatapattern.split(";");
        this.csvFormat = CSVFormat.RFC4180.withHeader(splittedFields);
        this.fields = Arrays.asList(splittedFields);

        GroupsAllocationConfig config = App.getInstance().getGroupsAllocationConfig();

        // if the pattern and the specified fields does not match
        if (!this.fields.contains(config.matchfield) || !this.fields.contains(config.groupfield)) {
            throw new BotException("Some specified fields does not match with the provided pattern");
        }

        // TODO: Y'a peut être d'autres trucs à check mais j'ai oublié
    }

    @Override
    public void processMessage(Message message) {
        if (!message.getContent().isPresent()) {
            // the bot cannot understand
            message.getChannel().subscribe(channel -> channel.createMessage(App.getInstance().getMessagesConfig().misunderstandmessage));
        }

        String csvFilePath = App.getInstance().getGroupsAllocationConfig().datafile.filepath;

        Iterable<CSVRecord> records;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(csvFilePath)))) {
            records = this.csvFormat.parse(reader);
        } catch (Exception e) {
            throw new BotException("An exception occured while reading the csv data file..", e);
        }

        Iterator<CSVRecord> recordIterator = records.iterator();
        while (recordIterator.hasNext()) {
            CSVRecord next = recordIterator.next();
            if (next.get(App.getInstance().getGroupsAllocationConfig().matchfield).equals(message.getContent().get())) {
                // if the message content matches with the 'check' field

                String groupFieldContent = next.get(App.getInstance().getGroupsAllocationConfig().groupfield);

                List<GroupType> types = App.getInstance().getGroupsAllocationConfig().grouptypes.stream()
                        .filter(groupType -> groupType.name.equals(groupFieldContent))
                        .collect(Collectors.toList());

                if (types.isEmpty()) {
                    // Type correspondant non trouvé
                }
            }
        }
    }

}
