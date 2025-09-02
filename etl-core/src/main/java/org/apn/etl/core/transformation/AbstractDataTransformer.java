package org.apn.etl.core.transformation;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Abstract base class for data transformers
 */
public abstract class AbstractDataTransformer implements DataTransformer {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final Dataset<Row> transform(Dataset<Row> input, Map<String, Object> parameters) {
        logger.info("Starting transformation: {}", getClass().getSimpleName());

        // Pre-transformation hook
        preTransform(input, parameters);

        // Main transformation logic
        Dataset<Row> result = doTransform(input, parameters);

        // Post-transformation hook
        postTransform(result, parameters);

        logger.info("Transformation completed: {}", getClass().getSimpleName());
        return result;
    }

    /**
     * Pre-transformation hook for setup operations
     */
    protected void preTransform(Dataset<Row> input, Map<String, Object> parameters) {
        // Default implementation does nothing
        logger.debug("Pre-transformation hook executed");
    }

    /**
     * Main transformation logic - must be implemented by subclasses
     */
    protected abstract Dataset<Row> doTransform(Dataset<Row> input, Map<String, Object> parameters);

    /**
     * Post-transformation hook for cleanup operations
     */
    protected void postTransform(Dataset<Row> output, Map<String, Object> parameters) {
        // Default implementation does nothing
        logger.debug("Post-transformation hook executed");
    }

    /**
     * Utility method to get parameter value with default
     */
    protected <T> T getParameter(Map<String, Object> parameters, String key, T defaultValue) {
        Object value = parameters.get(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return (T) value;
        } catch (ClassCastException e) {
            logger.warn("Parameter {} has wrong type, using default value", key);
            return defaultValue;
        }
    }
}