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

public class MessagesConfig {
    public String commandmissing;
    public String nodatamatch;
    public String allocationerror;
    public String misunderstandmessage;
    public String allocationmessage;
    public String alreadyallocated;

    public MessagesConfig() {
    }

    public MessagesConfig(String commandmissing, String nodatamatch, String allocationerror, String misunderstandmessage, String allocationmessage, String alreadyallocated) {
        this.commandmissing = commandmissing;
        this.nodatamatch = nodatamatch;
        this.allocationerror = allocationerror;
        this.misunderstandmessage = misunderstandmessage;
        this.allocationmessage = allocationmessage;
        this.alreadyallocated = alreadyallocated;
    }

    public String getCommandmissing() {
        return commandmissing;
    }

    public void setCommandmissing(String commandmissing) {
        this.commandmissing = commandmissing;
    }

    public String getNodatamatch() {
        return nodatamatch;
    }

    public void setNodatamatch(String nodatamatch) {
        this.nodatamatch = nodatamatch;
    }

    public String getAllocationerror() {
        return allocationerror;
    }

    public void setAllocationerror(String allocationerror) {
        this.allocationerror = allocationerror;
    }

    public String getMisunderstandmessage() {
        return misunderstandmessage;
    }

    public void setMisunderstandmessage(String misunderstandmessage) {
        this.misunderstandmessage = misunderstandmessage;
    }

    public String getAllocationmessage() {
        return allocationmessage;
    }

    public void setAllocationmessage(String allocationmessage) {
        this.allocationmessage = allocationmessage;
    }

    public String getAlreadyallocated() {
        return alreadyallocated;
    }

    public void setAlreadyallocated(String alreadyallocated) {
        this.alreadyallocated = alreadyallocated;
    }
}
