package org.apn.etl.core.factory;

import org.apn.etl.core.transformation.DataTransformer;
import org.apn.etl.core.transformation.SqlDataTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating data transformers.
 * This class provides a static method to instantiate data transformers, supporting both
 * built-in and custom transformer implementations.
 *
 * @author Amit Prakash Nema
 */
public class TransformerFactory {
    private static final Logger logger = LoggerFactory.getLogger(TransformerFactory.class);

    /**
     * Creates a {@link DataTransformer} instance based on the specified class name.
     * It can instantiate built-in transformers like "sql" or a custom transformer class by its fully qualified name.
     *
     * @param className The name of the transformer to create (e.g., "sql") or the fully qualified class name of a custom transformer.
     * @return A {@link DataTransformer} instance.
     * @throws RuntimeException if the transformer class cannot be found, instantiated, or accessed.
     */
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