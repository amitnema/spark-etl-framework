package org.apn.etl.core.model;

import java.util.Map;

/**
 * Input configuration model
 */
public class InputConfig {
    private String name;
    private String type; // file, database, stream, etc.
    private String format; // parquet, json, csv, avro, etc.
    private String path;
    private String connectionString;
    private String query;
    private Map<String, Object> options;
    private SchemaConfig schema;

    // Constructors
    public InputConfig() {}

    public InputConfig(String name, String type, String format, String path) {
        this.name = name;
        this.type = type;
        this.format = format;
        this.path = path;
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

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public Map<String, Object> getOptions() { return options; }
    public void setOptions(Map<String, Object> options) { this.options = options; }

    public SchemaConfig getSchema() { return schema; }
    public void setSchema(SchemaConfig schema) { this.schema = schema; }
}