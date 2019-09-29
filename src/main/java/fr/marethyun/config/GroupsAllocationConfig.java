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
