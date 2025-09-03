/*
* Copyright 2025 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
* See the License for the specific language governing permissions and
* limitations under the License
*/
package org.apn.etl.core.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

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
    ValidationResult.ValidationError error =
        new ValidationResult.ValidationError("test_rule", "test_column", "Test error message");
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
    ValidationResult.ValidationError error =
        new ValidationResult.ValidationError(ruleName, column, message);

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
