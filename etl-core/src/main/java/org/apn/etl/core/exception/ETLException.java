package org.apn.etl.core.exception;

/**
 * Base exception class for ETL framework
 */
public class ETLException extends Exception {

    public ETLException(String message) {
        super(message);
    }

    public ETLException(String message, Throwable cause) {
        super(message, cause);
    }
}