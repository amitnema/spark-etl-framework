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

import java.util.List;
import java.util.Map;
import lombok.*;

/**
 * Result of data validation in the ETL framework.
 *
 * <p>Contains validation status, error details, metrics, and record counts.
 *
 * @author Amit Prakash Nema
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public final class ValidationResult {
  private boolean isValid;
  private List<ValidationError> errors;
  private Map<String, Object> metrics;
  private long recordsProcessed;
  private long recordsFailed;

  /**
   * Represents a validation error for a specific rule and column.
   *
   * @author Amit Prakash Nema
   */
  @Data
  @RequiredArgsConstructor
  @AllArgsConstructor
  @Builder
  public static final class ValidationError {
    private final String ruleName;
    private final String column;
    private final String message;
    private Object value;
  }
}
