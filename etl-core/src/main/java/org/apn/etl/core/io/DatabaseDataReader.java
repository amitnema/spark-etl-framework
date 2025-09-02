package org.apn.etl.core.io;

import org.apn.etl.core.config.SparkConfig;
import org.apn.etl.core.model.InputConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Database data reader implementation.
 * @author Amit Prakash Nema
 */
public class DatabaseDataReader implements DataReader {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataReader.class);

    /**
     * Reads data from a database source based on the provided configuration.
     *
     * @param config The input configuration for the database source.
     * @return A Spark Dataset containing the data from the database.
     * @throws IllegalArgumentException if the connection string is missing.
     */
    @Override
    public Dataset<Row> read(InputConfig config) {
        SparkSession spark = SparkConfig.getSparkSession();

        String connectionString = config.getConnectionString();
        String query = config.getQuery();

        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalArgumentException("Connection string is required for database reader");
        }

        logger.info("Reading from database: {}", connectionString);

        Properties connectionProps = new Properties();

        // Add connection properties from options
        if (config.getOptions() != null) {
            config.getOptions().forEach((key, value) ->
                connectionProps.setProperty(key, String.valueOf(value)));
        }

        if (query != null && !query.isEmpty()) {
            // Read with custom query
            return spark.read()
                .jdbc(connectionString, String.format("(%s) as query_table", query), connectionProps);
        } else {
            // Read entire table
            String tableName = config.getPath(); // Using path as table name
            return spark.read()
                .jdbc(connectionString, tableName, connectionProps);
        }
    }
}