package org.apn.etl.core.exception;

/**
 * Exception thrown during data transformation
 */
public class TransformationException extends ETLException {

    public TransformationException(String message) {
        super(message);
    }

    public TransformationException(String message, Throwable cause) {
        super(message, cause);
    }
}