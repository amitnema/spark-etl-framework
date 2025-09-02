package org.apn.etl.core.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apn.etl.core.config.SparkConfig;

import java.util.Map;

/**
 * SQL-based data transformer
 */
public class SqlDataTransformer extends AbstractDataTransformer {

    @Override
    protected Dataset<Row> doTransform(Dataset<Row> input, Map<String, Object> parameters) {
        SparkSession spark = SparkConfig.getSparkSession();

        String sql = getParameter(parameters, "sql", "");
        String tempViewName = getParameter(parameters, "tempViewName", "input_data");

        if (sql.isEmpty()) {
            throw new IllegalArgumentException("SQL query is required for SqlDataTransformer");
        }

        logger.info("Executing SQL transformation with temp view: {}", tempViewName);
        logger.debug("SQL Query: {}", sql);

        // Create temporary view
        input.createOrReplaceTempView(tempViewName);

        try {
            // Execute SQL query
            return spark.sql(sql);
        } catch (Exception e) {
            logger.error("Error executing SQL query", e);
            throw new RuntimeException("SQL transformation failed", e);
        }
    }
}