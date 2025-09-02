package org.apn.etl.core.io;

import org.apn.etl.core.model.OutputConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Database data writer implementation
 */
public class DatabaseDataWriter implements DataWriter {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataWriter.class);

    @Override
    public void write(Dataset<Row> dataset, OutputConfig config) {
        String connectionString = config.getConnectionString();
        String tableName = config.getPath(); // Using path as table name
        SaveMode mode = getSaveMode(config.getMode());

        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalArgumentException("Connection string is required for database writer");
        }

        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name is required for database writer");
        }

        logger.info("Writing to database table: {} with mode: {}", tableName, mode);

        Properties connectionProps = new Properties();

        // Add connection properties from options
        if (config.getOptions() != null) {
            config.getOptions().forEach((key, value) -> 
                connectionProps.setProperty(key, String.valueOf(value)));
        }

        dataset.write()
            .mode(mode)
            .jdbc(connectionString, tableName, connectionProps);
    }

    private SaveMode getSaveMode(String mode) {
        if (mode == null) return SaveMode.ErrorIfExists;

        switch (mode.toLowerCase()) {
            case "overwrite": return SaveMode.Overwrite;
            case "append": return SaveMode.Append;
            case "ignore": return SaveMode.Ignore;
            case "error": 
            default: return SaveMode.ErrorIfExists;
        }
    }
}