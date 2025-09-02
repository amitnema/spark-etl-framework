package org.apn.etl.jobs.sample;

import org.apn.etl.core.transformation.AbstractDataTransformer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

import java.util.Map;

/**
 * Sample data transformer implementation
 */
public class SampleDataTransformer extends AbstractDataTransformer {

    @Override
    protected Dataset<Row> doTransform(Dataset<Row> input, Map<String, Object> parameters) {
        logger.info("Executing sample data transformation");

        // Sample transformation: Add current timestamp and filter active records
        Dataset<Row> transformed = input
            .withColumn("processed_timestamp", functions.current_timestamp())
            .withColumn("year", functions.year(functions.col("date_column")))
            .filter(functions.col("status").equalTo("active"));

        logger.info("Sample transformation completed. Input records: {}, Output records: {}", 
            input.count(), transformed.count());

        return transformed;
    }
}