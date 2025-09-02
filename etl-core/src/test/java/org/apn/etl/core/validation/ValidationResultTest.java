package org.apn.etl.core.validation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    @Test
    void testValidationResultCreation() {
        // Given
        ValidationResult result = new ValidationResult(true);

        // Then
        assertTrue(result.isValid());
        assertNull(result.getErrors());
        assertNull(result.getMetrics());
        assertEquals(0, result.getRecordsProcessed());
        assertEquals(0, result.getRecordsFailed());
    }

    @Test
    void testValidationResultWithErrors() {
        // Given
        ValidationResult result = new ValidationResult();
        List<ValidationResult.ValidationError> errors = new ArrayList<>();
        ValidationResult.ValidationError error = new ValidationResult.ValidationError(
            "test_rule", "test_column", "Test error message");
        errors.add(error);

        // When
        result.setValid(false);
        result.setErrors(errors);
        result.setRecordsProcessed(100);
        result.setRecordsFailed(5);

        // Then
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals(100, result.getRecordsProcessed());
        assertEquals(5, result.getRecordsFailed());
    }

    @Test
    void testValidationErrorCreation() {
        // Given
        String ruleName = "test_rule";
        String column = "test_column";
        String message = "Test validation error";

        // When
        ValidationResult.ValidationError error = new ValidationResult.ValidationError(ruleName, column, message);

        // Then
        assertEquals(ruleName, error.getRuleName());
        assertEquals(column, error.getColumn());
        assertEquals(message, error.getMessage());
        assertNull(error.getValue());
    }

    @Test
    void testValidationResultWithMetrics() {
        // Given
        ValidationResult result = new ValidationResult();
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("null_count", 5L);
        metrics.put("unique_count", 95L);

        // When
        result.setMetrics(metrics);

        // Then
        assertEquals(2, result.getMetrics().size());
        assertEquals(5L, result.getMetrics().get("null_count"));
        assertEquals(95L, result.getMetrics().get("unique_count"));
    }
}