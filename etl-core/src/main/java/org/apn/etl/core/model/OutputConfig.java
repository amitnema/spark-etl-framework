package org.apn.etl.core.model;

import java.util.Map;

/**
 * Output configuration model
 */
public class OutputConfig {
    private String name;
    private String type; // file, database, stream, etc.
    private String format; // parquet, json, csv, etc.
    private String path;
    private String connectionString;
    private String mode; // overwrite, append, ignore, error
    private Map<String, Object> options;
    private PartitionConfig partition;

    // Constructors
    public OutputConfig() {}

    public OutputConfig(String name, String type, String format, String path, String mode) {
        this.name = name;
        this.type = type;
        this.format = format;
        this.path = path;
        this.mode = mode;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getConnectionString() { return connectionString; }
    public void setConnectionString(String connectionString) { this.connectionString = connectionString; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }

    public PartitionConfig getPartition() { return partition; }
    public void setPartition(PartitionConfig partition) { this.partition = partition; }
}