package org.apn.etl.core.io;

import org.apn.etl.core.model.InputConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Interface for data readers
 */
public interface DataReader {
    Dataset<Row> read(InputConfig config);
}