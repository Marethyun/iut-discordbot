package fr.marethyun.config;

public class MessagesConfig {
    public String authormissing;
    public String commandmissing;
    public String allocationmessage;
    public String misunderstandmessage;
    public String error;
    public String nodatamatch;

    public MessagesConfig() {
    }

    public MessagesConfig(String authormissing, String commandmissing, String allocationmessage, String misunderstandmessage, String error, String nodatamatch) {
        this.authormissing = authormissing;
        this.commandmissing = commandmissing;
        this.allocationmessage = allocationmessage;
        this.misunderstandmessage = misunderstandmessage;
        this.error = error;
        this.nodatamatch = nodatamatch;
    }

    public String getAuthormissing() {
        return authormissing;
    }

    public void setAuthormissing(String authormissing) {
        this.authormissing = authormissing;
    }

    public String getCommandmissing() {
        return commandmissing;
    }

    public void setCommandmissing(String commandmissing) {
        this.commandmissing = commandmissing;
    }

    public String getAllocationmessage() {
        return allocationmessage;
    }

    public void setAllocationmessage(String allocationmessage) {
        this.allocationmessage = allocationmessage;
    }

    public String getMisunderstandmessage() {
        return misunderstandmessage;
    }

    public void setMisunderstandmessage(String misunderstandmessage) {
        this.misunderstandmessage = misunderstandmessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getNodatamatch() {
        return nodatamatch;
    }

    public void setNodatamatch(String nodatamatch) {
        this.nodatamatch = nodatamatch;
    }
}
