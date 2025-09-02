package org.apn.etl.core.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import java.util.Map;

/**
 * Interface for data transformers
 */
public interface DataTransformer {
    Dataset<Row> transform(Dataset<Row> input, Map<String, Object> parameters);
}