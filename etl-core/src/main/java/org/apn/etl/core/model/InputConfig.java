package org.apn.etl.core.model;

import java.util.Map;

/**
 * Input configuration model for ETL jobs.
 * <p>
 * Defines the source input details including type, format, path, connection, query, options, and schema.
 * </p>
 *
 * @author Amit Prakash Nema
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
    /**
     * Gets the input name.
     * @return input name
     */
    public String getName() { return name; }
    /**
     * Sets the input name.
     * @param name input name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the input type (file, database, stream, etc.).
     * @return input type
     */
    public String getType() { return type; }
    /**
     * Sets the input type.
     * @param type input type
     */
    public void setType(String type) { this.type = type; }

    /**
     * Gets the input format (parquet, json, csv, avro, etc.).
     * @return input format
     */
    public String getFormat() { return format; }
    /**
     * Sets the input format.
     * @param format input format
     */
    public void setFormat(String format) { this.format = format; }

    /**
     * Gets the input path.
     * @return input path
     */
    public String getPath() { return path; }
    /**
     * Sets the input path.
     * @param path input path
     */
    public void setPath(String path) { this.path = path; }

    /**
     * Gets the connection string for database inputs.
     * @return connection string
     */
    public String getConnectionString() { return connectionString; }
    /**
     * Sets the connection string for database inputs.
     * @param connectionString connection string
     */
    public void setConnectionString(String connectionString) { this.connectionString = connectionString; }

    /**
     * Gets the query for database inputs.
     * @return query string
     */
    public String getQuery() { return query; }
    /**
     * Sets the query for database inputs.
     * @param query query string
     */
    public void setQuery(String query) { this.query = query; }

    /**
     * Gets additional input options.
     * @return options map
     */
    public Map<String, Object> getOptions() { return options; }
    /**
     * Sets additional input options.
     * @param options options map
     */
    public void setOptions(Map<String, Object> options) { this.options = options; }

    /**
     * Gets the schema configuration for the input.
     * @return schema config
     */
    public SchemaConfig getSchema() { return schema; }
    /**
     * Sets the schema configuration for the input.
     * @param schema schema config
     */
    public void setSchema(SchemaConfig schema) { this.schema = schema; }
}