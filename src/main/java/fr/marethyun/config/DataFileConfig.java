package fr.marethyun.config;

public final class DataFileConfig {
    public String filepath;
    public String rawdatapattern;

    public DataFileConfig() {
    }

    public DataFileConfig(String filepath, String rawdatapattern) {
        this.filepath = filepath;
        this.rawdatapattern = rawdatapattern;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getRawdatapattern() {
        return rawdatapattern;
    }

    public void setRawdatapattern(String rawdatapattern) {
        this.rawdatapattern = rawdatapattern;
    }
}