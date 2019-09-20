package fr.marethyun.config;

import java.util.ArrayList;

public final class GroupsAllocationConfig {

    public DataFileConfig datafile;
    public String guildid;
    public String matchfield;
    public String groupfield;

    public ArrayList<GroupType> grouptypes;

    public GroupsAllocationConfig() {
    }

    public GroupsAllocationConfig(DataFileConfig datafile, String guildid, String matchfield, String groupfield, String allocationmessage, String misunderstandmessage, ArrayList<GroupType> grouptypes) {
        this.datafile = datafile;
        this.guildid = guildid;
        this.matchfield = matchfield;
        this.groupfield = groupfield;
        this.grouptypes = grouptypes;
    }

    public DataFileConfig getDatafile() {
        return datafile;
    }

    public void setDatafile(DataFileConfig datafile) {
        this.datafile = datafile;
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
