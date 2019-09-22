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
