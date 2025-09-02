package org.apn.etl.core.model;

import java.util.Map;

/**
 * Output configuration model for ETL jobs.
 * <p>
 * Defines the target output details including type, format, path, connection, mode, options, and partitioning.
 * </p>
 *
 * @author Amit Prakash Nema
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
    /**
     * Gets the output name.
     * @return output name
     */
    public String getName() { return name; }
    /**
     * Sets the output name.
     * @param name output name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the output type (file, database, stream, etc.).
     * @return output type
     */
    public String getType() { return type; }
    /**
     * Sets the output type.
     * @param type output type
     */
    public void setType(String type) { this.type = type; }

    /**
     * Gets the output format (parquet, json, csv, etc.).
     * @return output format
     */
    public String getFormat() { return format; }
    /**
     * Sets the output format.
     * @param format output format
     */
    public void setFormat(String format) { this.format = format; }

    /**
     * Gets the output path.
     * @return output path
     */
    public String getPath() { return path; }
    /**
     * Sets the output path.
     * @param path output path
     */
    public void setPath(String path) { this.path = path; }

    /**
     * Gets the connection string for database outputs.
     * @return connection string
     */
    public String getConnectionString() { return connectionString; }
    /**
     * Sets the connection string for database outputs.
     * @param connectionString connection string
     */
    public void setConnectionString(String connectionString) { this.connectionString = connectionString; }

    /**
     * Gets the output mode (overwrite, append, ignore, error).
     * @return output mode
     */
    public String getMode() { return mode; }
    /**
     * Sets the output mode.
     * @param mode output mode
     */
    public void setMode(String mode) { this.mode = mode; }

    /**
     * Gets additional output options.
     * @return options map
     */
    public Map<String, Object> getOptions() { return options; }
    /**
     * Sets additional output options.
     * @param options options map
     */
    public void setOptions(Map<String, Object> options) { this.options = options; }

    /**
     * Gets the partition configuration for the output.
     * @return partition config
     */
    public PartitionConfig getPartition() { return partition; }
    /**
     * Sets the partition configuration for the output.
     * @param partition partition config
     */
    public void setPartition(PartitionConfig partition) { this.partition = partition; }
}