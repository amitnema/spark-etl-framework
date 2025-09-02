package org.apn.etl.core.exception;

/**
 * Base exception class for the ETL framework.
 * This is the general exception thrown for errors during the ETL process.
 *
 * @author Amit Prakash Nema
 */
public class ETLException extends Exception {

    /**
     * Constructs a new ETLException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ETLException(String message) {
        super(message);
    }

    /**
     * Constructs a new ETLException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ETLException(String message, Throwable cause) {
        super(message, cause);
    }
}