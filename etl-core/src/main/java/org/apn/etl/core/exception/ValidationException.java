package org.apn.etl.core.exception;

import org.apn.etl.core.validation.ValidationResult;

/**
 * Exception thrown when data validation fails
 */
public class ValidationException extends ETLException {
    private final ValidationResult validationResult;

    public ValidationException(String message, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}