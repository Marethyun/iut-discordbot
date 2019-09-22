package fr.marethyun.config;

import java.util.ArrayList;

public final class GroupsAllocationConfig {

    public String rawdatapattern;
    public String guildid;
    public String matchfield;
    public String groupfield;

    public ArrayList<GroupType> grouptypes;

    public GroupsAllocationConfig() {
    }

    public GroupsAllocationConfig(String rawdatapattern, String guildid, String matchfield, String groupfield, ArrayList<GroupType> grouptypes) {
        this.rawdatapattern = rawdatapattern;
        this.guildid = guildid;
        this.matchfield = matchfield;
        this.groupfield = groupfield;
        this.grouptypes = grouptypes;
    }

    public String getRawdatapattern() {
        return rawdatapattern;
    }

    public void setRawdatapattern(String rawdatapattern) {
        this.rawdatapattern = rawdatapattern;
    }

    public String getGuildid() {
        return guildid;
    }

    public void setGuildid(String guildid) {
        this.guildid = guildid;
    }

    public String getMatchfield() {
        return matchfield;
    }

    public void setMatchfield(String matchfield) {
        this.matchfield = matchfield;
    }

    public String getGroupfield() {
        return groupfield;
    }

    public void setGroupfield(String groupfield) {
        this.groupfield = groupfield;
    }

    public ArrayList<GroupType> getGrouptypes() {
        return grouptypes;
    }

    public void setGrouptypes(ArrayList<GroupType> grouptypes) {
        this.grouptypes = grouptypes;
    }
}
