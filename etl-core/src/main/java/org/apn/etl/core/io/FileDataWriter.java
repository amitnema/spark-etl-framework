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
 * File-based data writer implementation
 */
public class FileDataWriter implements DataWriter {
    private static final Logger logger = LoggerFactory.getLogger(FileDataWriter.class);

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