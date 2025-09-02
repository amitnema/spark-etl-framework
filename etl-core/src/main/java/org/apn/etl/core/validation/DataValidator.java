package org.apn.etl.core.validation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Interface for data validators
 */
public interface DataValidator {
    ValidationResult validate(Dataset<Row> dataset);
}