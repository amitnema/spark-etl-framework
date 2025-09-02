package org.apn.etl.core.io;

import org.apn.etl.core.model.OutputConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Interface for data writers
 */
public interface DataWriter {
    void write(Dataset<Row> dataset, OutputConfig config);
}