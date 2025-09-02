package org.apn.etl.core.io;

import org.apn.etl.core.model.OutputConfig;
import org.apn.etl.core.model.PartitionConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.DataFrameWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apn.etl.core.utils.ETLUtils.toStringMap;

/**
 * File-based data writer implementation.
 * @author Amit Prakash Nema
 */
public class FileDataWriter implements DataWriter {
    private static final Logger logger = LoggerFactory.getLogger(FileDataWriter.class);

    /**
     * Writes a Spark Dataset to a file-based destination.
     *
     * @param dataset The Dataset to write.
     * @param config  The output configuration specifying format, path, save mode, etc.
     * @throws IllegalArgumentException if the file format in the config is not supported.
     */
    @Override
    public void write(Dataset<Row> dataset, OutputConfig config) {
        String format = config.getFormat().toLowerCase();
        String path = config.getPath();
        SaveMode mode = getSaveMode(config.getMode());

        logger.info("Writing {} file to path: {} with mode: {}", format, path, mode);

        DataFrameWriter<Row> writer = dataset.write()
            .mode(mode)
            .options(toStringMap(config.getOptions()));

        // Apply partitioning if configured
        if (config.getPartition() != null) {
            writer = applyPartitioning(writer, config.getPartition());
        }

        switch (format) {
            case "parquet":
                writer.parquet(path);
                break;
            case "json":
                writer.json(path);
                break;
            case "csv":
                writer.option("header", "true").csv(path);
                break;
            case "avro":
                writer.format("avro").save(path);
                break;
            case "orc":
                writer.orc(path);
                break;
            default:
                throw new IllegalArgumentException("Unsupported file format: " + format);
        }
    }

    /**
     * Converts a string representation of a save mode to the Spark SaveMode enum.
     *
     * @param mode The save mode as a string (e.g., "overwrite", "append").
     * @return The corresponding Spark SaveMode enum. Defaults to ErrorIfExists.
     */
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

    /**
     * Applies partitioning to the DataFrameWriter based on the provided configuration.
     *
     * @param writer    The DataFrameWriter to configure.
     * @param partition The partitioning configuration.
     * @return The configured DataFrameWriter.
     */
    private DataFrameWriter<Row> applyPartitioning(DataFrameWriter<Row> writer, PartitionConfig partition) {
        if (partition.getColumns() != null && !partition.getColumns().isEmpty()) {
            return writer.partitionBy(partition.getColumns().toArray(new String[0]));
        }

        if (partition.getNumPartitions() > 0) {
            // This would need to be handled at dataset level before writing
            logger.warn("Number of partitions should be set before calling write operation");
        }

        return writer;
    }
}