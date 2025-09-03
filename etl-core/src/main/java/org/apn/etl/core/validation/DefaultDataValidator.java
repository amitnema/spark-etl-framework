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
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License
*/
package org.apn.etl.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apn.etl.core.model.ValidationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default data validator implementation.
 *
 * @author Amit Prakash Nema
 */
public final class DefaultDataValidator implements DataValidator {
  private static final Logger logger = LoggerFactory.getLogger(DefaultDataValidator.class);

  private final ValidationConfig config;

  public DefaultDataValidator(final ValidationConfig config) {
    this.config = config;
  }

  @Override
  public ValidationResult validate(final Dataset<Row> dataset) {
    if (!config.isEnabled()) {
      logger.info("Validation is disabled, skipping validation");
      return new ValidationResult(true);
    }

    final ValidationResult result = new ValidationResult();
    final List<ValidationResult.ValidationError> errors = new ArrayList<>();
    final Map<String, Object> metrics = new HashMap<>();

    final long totalRecords = dataset.count();
    result.setRecordsProcessed(totalRecords);

    logger.info("Starting validation for {} records", totalRecords);

    // Execute validation rules
    for (final ValidationConfig.ValidationRule rule : config.getRules()) {
      try {
        validateRule(dataset, rule, errors, metrics);
      } catch (final Exception e) {
        logger.error("Error executing validation rule: {}", rule.getName(), e);
        errors.add(
            new ValidationResult.ValidationError(
                rule.getName(),
                rule.getColumn(),
                "Validation rule execution failed: " + e.getMessage()));
      }
    }

    result.setErrors(errors);
    result.setMetrics(metrics);
    result.setRecordsFailed(errors.size());
    result.setValid(errors.isEmpty());

    logger.info("Validation completed. Valid: {}, Errors: {}", result.isValid(), errors.size());

    return result;
  }

  private void validateRule(
      final Dataset<Row> dataset,
      final ValidationConfig.ValidationRule rule,
      final List<ValidationResult.ValidationError> errors,
      final Map<String, Object> metrics) {

    final String ruleType = rule.getType().toUpperCase();

    switch (ruleType) {
      case "NOT_NULL":
        validateNotNull(dataset, rule, errors, metrics);
        break;
      case "UNIQUE":
        validateUnique(dataset, rule, errors, metrics);
        break;
      case "RANGE":
        validateRange(dataset, rule, errors, metrics);
        break;
      case "REGEX":
        validateRegex(dataset, rule, errors, metrics);
        break;
      default:
        logger.warn("Unknown validation rule type: {}", ruleType);
    }
  }

  private void validateNotNull(
      final Dataset<Row> dataset,
      final ValidationConfig.ValidationRule rule,
      final List<ValidationResult.ValidationError> errors,
      final Map<String, Object> metrics) {
    final String column = rule.getColumn();
    final long nullCount = dataset.filter(functions.col(column).isNull()).count();

    metrics.put(rule.getName() + "_null_count", nullCount);

    if (nullCount > 0) {
      errors.add(
          new ValidationResult.ValidationError(
              rule.getName(),
              column,
              String.format("Found %d null values in column %s", nullCount, column)));
    }
  }

  private void validateUnique(
      final Dataset<Row> dataset,
      final ValidationConfig.ValidationRule rule,
      final List<ValidationResult.ValidationError> errors,
      final Map<String, Object> metrics) {
    final String column = rule.getColumn();
    final long totalCount = dataset.count();
    final long uniqueCount = dataset.select(column).distinct().count();

    metrics.put(rule.getName() + "_unique_count", uniqueCount);
    metrics.put(rule.getName() + "_duplicate_count", totalCount - uniqueCount);

    if (totalCount != uniqueCount) {
      errors.add(
          new ValidationResult.ValidationError(
              rule.getName(),
              column,
              String.format(
                  "Found %d duplicate values in column %s", totalCount - uniqueCount, column)));
    }
  }

  private void validateRange(
      final Dataset<Row> dataset,
      final ValidationConfig.ValidationRule rule,
      final List<ValidationResult.ValidationError> errors,
      final Map<String, Object> metrics) {
    // Implementation for range validation
    logger.info("Range validation for rule: {}", rule.getName());
    // Add specific range validation logic here
  }

  private void validateRegex(
      final Dataset<Row> dataset,
      final ValidationConfig.ValidationRule rule,
      final List<ValidationResult.ValidationError> errors,
      final Map<String, Object> metrics) {
    // Implementation for regex validation
    logger.info("Regex validation for rule: {}", rule.getName());
    // Add specific regex validation logic here
  }
}
