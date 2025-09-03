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

import java.util.List;
import java.util.Map;

/**
 * Result of data validation in the ETL framework.
 *
 * <p>Contains validation status, error details, metrics, and record counts.
 *
 * @author Amit Prakash Nema
 */
public final class ValidationResult {
  private boolean isValid;
  private List<ValidationError> errors;
  private Map<String, Object> metrics;
  private long recordsProcessed;
  private long recordsFailed;

  public ValidationResult() {}

  public ValidationResult(final boolean isValid) {
    this.isValid = isValid;
  }

  /**
   * Gets the validation status.
   *
   * @return true if valid, false otherwise
   */
  public boolean isValid() {
    return isValid;
  }

  /**
   * Sets the validation status.
   *
   * @param valid true if valid, false otherwise
   */
  public void setValid(final boolean valid) {
    isValid = valid;
  }

  /**
   * Gets the list of validation errors.
   *
   * @return list of errors
   */
  public List<ValidationError> getErrors() {
    return errors;
  }

  /**
   * Sets the list of validation errors.
   *
   * @param errors list of errors
   */
  public void setErrors(final List<ValidationError> errors) {
    this.errors = errors;
  }

  /**
   * Gets validation metrics.
   *
   * @return metrics map
   */
  public Map<String, Object> getMetrics() {
    return metrics;
  }

  /**
   * Sets validation metrics.
   *
   * @param metrics metrics map
   */
  public void setMetrics(final Map<String, Object> metrics) {
    this.metrics = metrics;
  }

  /**
   * Gets the number of records processed.
   *
   * @return processed record count
   */
  public long getRecordsProcessed() {
    return recordsProcessed;
  }

  /**
   * Sets the number of records processed.
   *
   * @param recordsProcessed processed record count
   */
  public void setRecordsProcessed(final long recordsProcessed) {
    this.recordsProcessed = recordsProcessed;
  }

  /**
   * Gets the number of records failed.
   *
   * @return failed record count
   */
  public long getRecordsFailed() {
    return recordsFailed;
  }

  /**
   * Sets the number of records failed.
   *
   * @param recordsFailed failed record count
   */
  public void setRecordsFailed(final long recordsFailed) {
    this.recordsFailed = recordsFailed;
  }

  /**
   * Represents a validation error for a specific rule and column.
   *
   * @author Amit Prakash Nema
   */
  public static final class ValidationError {
    private String ruleName;
    private String column;
    private String message;
    private Object value;

    public ValidationError() {}

    public ValidationError(final String ruleName, final String column, final String message) {
      this.ruleName = ruleName;
      this.column = column;
      this.message = message;
    }

    // Getters and Setters
    public String getRuleName() {
      return ruleName;
    }

    public void setRuleName(final String ruleName) {
      this.ruleName = ruleName;
    }

    public String getColumn() {
      return column;
    }

    public void setColumn(final String column) {
      this.column = column;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(final String message) {
      this.message = message;
    }

    public Object getValue() {
      return value;
    }

    public void setValue(final Object value) {
      this.value = value;
    }
  }
}
