package org.apn.etl.core.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import java.util.Map;

/**
 * Interface for data transformers in the ETL framework.
 * <p>
 * Implementations should provide logic to transform a Spark {@link Dataset} using provided parameters.
 * </p>
 *
 * @author Amit Prakash Nema
 */
public interface DataTransformer {
    /**
     * Transforms the input dataset using the provided parameters.
     *
     * @param input      Input dataset
     * @param parameters Transformation parameters
     * @return Transformed dataset
     */
    Dataset<Row> transform(Dataset<Row> input, Map<String, Object> parameters);
}