package org.apn.etl.core.factory;

import org.apn.etl.core.io.DataReader;
import org.apn.etl.core.io.DataWriter;
import org.apn.etl.core.io.FileDataReader;
import org.apn.etl.core.io.FileDataWriter;
import org.apn.etl.core.io.DatabaseDataReader;
import org.apn.etl.core.io.DatabaseDataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating data readers and writers.
 * This class provides static methods to instantiate appropriate data reader and writer
 * implementations based on the specified type.
 *
 * @author Amit Prakash Nema
 */
public class IOFactory {
    private static final Logger logger = LoggerFactory.getLogger(IOFactory.class);

    /**
     * Creates a {@link DataReader} instance based on the specified type.
     *
     * @param type The type of the reader to create (e.g., "file", "database").
     * @return A {@link DataReader} instance.
     * @throws IllegalArgumentException if the reader type is not supported.
     */
    public static DataReader createReader(String type) {
        logger.info("Creating reader for type: {}", type);

        switch (type.toLowerCase()) {
            case "file":
            case "filesystem":
                return new FileDataReader();
            case "database":
            case "jdbc":
                return new DatabaseDataReader();
            default:
                throw new IllegalArgumentException("Unsupported reader type: " + type);
        }
    }

    /**
     * Creates a {@link DataWriter} instance based on the specified type.
     *
     * @param type The type of the writer to create (e.g., "file", "database").
     * @return A {@link DataWriter} instance.
     * @throws IllegalArgumentException if the writer type is not supported.
     */
    public static DataWriter createWriter(String type) {
        logger.info("Creating writer for type: {}", type);

        switch (type.toLowerCase()) {
            case "file":
            case "filesystem":
                return new FileDataWriter();
            case "database":
            case "jdbc":
                return new DatabaseDataWriter();
            default:
                throw new IllegalArgumentException("Unsupported writer type: " + type);
        }
    }
}