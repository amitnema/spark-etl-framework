package org.apn.etl.core.io;

import org.apn.etl.core.model.InputConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Interface for data readers
 * @author Amit Prakash Nema
 */
public interface DataReader {
    /**
     * Reads data from a source defined by the input configuration.
     *
     * @param config The input configuration.
     * @return A Spark Dataset containing the data.
     */
    Dataset<Row> read(InputConfig config);
}