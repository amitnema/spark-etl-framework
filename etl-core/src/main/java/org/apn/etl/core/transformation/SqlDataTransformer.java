package org.apn.etl.core.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apn.etl.core.config.SparkConfig;

import java.util.Map;

/**
 * SQL-based data transformer for executing custom SQL queries on Spark datasets.
 * <p>
 * This transformer creates a temporary view from the input dataset and executes the provided SQL query.
 * </p>
 *
 * <b>Parameters:</b>
 * <ul>
 *   <li>sql - The SQL query to execute (required)</li>
 *   <li>tempViewName - The name of the temporary view (optional, default: "input_data")</li>
 * </ul>
 *
 * @author Amit Prakash Nema
 */
public class SqlDataTransformer extends AbstractDataTransformer {
    /**
     * Executes the SQL transformation on the input dataset.
     *
     * @param input      Input dataset
     * @param parameters Transformation parameters (must include "sql")
     * @return Transformed dataset after SQL execution
     * @throws IllegalArgumentException if SQL query is missing
     * @throws RuntimeException if SQL execution fails
     */
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