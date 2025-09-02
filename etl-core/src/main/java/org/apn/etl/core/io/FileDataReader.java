package org.apn.etl.core.io;

import org.apn.etl.core.config.SparkConfig;
import org.apn.etl.core.model.InputConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apn.etl.core.utils.ETLUtils.toStringMap;

/**
 * File-based data reader implementation.
 * @author Amit Prakash Nema
 */
public class FileDataReader implements DataReader {
    private static final Logger logger = LoggerFactory.getLogger(FileDataReader.class);

    /**
     * Reads data from a file source based on the provided configuration.
     *
     * @param config The input configuration for the file source.
     * @return A Spark Dataset containing the data from the file.
     * @throws IllegalArgumentException if the file format in the config is not supported.
     */
    @Override
    public Dataset<Row> read(InputConfig config) {
        SparkSession spark = SparkConfig.getSparkSession();
        String format = config.getFormat().toLowerCase();
        String path = config.getPath();

        logger.info("Reading {} file from path: {}", format, path);

        switch (format) {
            case "parquet":
                return readParquet(spark, config);
            case "json":
                return readJson(spark, config);
            case "csv":
                return readCsv(spark, config);
            case "avro":
                return readAvro(spark, config);
            case "orc":
                return readOrc(spark, config);
            default:
                throw new IllegalArgumentException("Unsupported file format: " + format);
        }
    }

    private Dataset<Row> readParquet(SparkSession spark, InputConfig config) {
        return spark.read()
            .options(toStringMap(config.getOptions()))
            .parquet(config.getPath());
    }

    private Dataset<Row> readJson(SparkSession spark, InputConfig config) {
        return spark.read()
            .options(toStringMap(config.getOptions()))
            .json(config.getPath());
    }

    private Dataset<Row> readCsv(SparkSession spark, InputConfig config) {
        return spark.read()
            .option("header", "true")
            .option("inferSchema", "true")
            .options(toStringMap(config.getOptions()))
            .csv(config.getPath());
    }

    private Dataset<Row> readAvro(SparkSession spark, InputConfig config) {
        return spark.read()
            .format("avro")
            .options(toStringMap(config.getOptions()))
            .load(config.getPath());
    }

    private Dataset<Row> readOrc(SparkSession spark, InputConfig config) {
        return spark.read()
            .options(toStringMap(config.getOptions()))
            .orc(config.getPath());
    }
}