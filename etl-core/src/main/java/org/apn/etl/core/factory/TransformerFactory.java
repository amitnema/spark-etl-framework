package org.apn.etl.core.factory;

import org.apn.etl.core.transformation.DataTransformer;
import org.apn.etl.core.transformation.SqlDataTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating data transformers
 */
public class TransformerFactory {
    private static final Logger logger = LoggerFactory.getLogger(TransformerFactory.class);

    public static DataTransformer createTransformer(String className) {
        logger.info("Creating transformer: {}", className);

        try {
            // Handle built-in transformers
            switch (className.toLowerCase()) {
                case "sql":
                case "sqldatatransformer":
                    return new SqlDataTransformer();
                default:
                    // Try to load custom transformer class
                    Class<?> clazz = Class.forName(className);
                    return (DataTransformer) clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            logger.error("Error creating transformer: {}", className, e);
            throw new RuntimeException("Failed to create transformer: " + className, e);
        }
    }
}