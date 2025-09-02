package org.apn.etl.core.io;

import org.apn.etl.core.model.OutputConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Interface for data writers.
 * @author Amit Prakash Nema
 */
public interface DataWriter {
    /**
     * Writes a Spark Dataset to a destination defined by the output configuration.
     *
     * @param dataset The Dataset to write.
     * @param config  The output configuration.
     */
    void write(Dataset<Row> dataset, OutputConfig config);
}