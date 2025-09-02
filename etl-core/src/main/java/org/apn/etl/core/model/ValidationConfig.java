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
package org.apn.etl.core.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validation configuration model for ETL jobs.
 *
 * <p>Defines validation rules, parameters, and failure handling strategy.
 *
 * @author Amit Prakash Nema
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationConfig {
  private boolean enabled;
  private List<ValidationRule> rules;
  private String onFailure; // STOP, CONTINUE, QUARANTINE
  private Map<String, Object> parameters;

  /**
   * Validation rule configuration for ETL jobs.
   *
   * @author Amit Prakash Nema
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ValidationRule {
    private String name;
    private String type; // NOT_NULL, UNIQUE, RANGE, REGEX, CUSTOM
    private String column;
    private Map<String, Object> parameters;
  }
}
