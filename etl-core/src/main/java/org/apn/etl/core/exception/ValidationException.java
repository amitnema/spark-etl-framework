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
package org.apn.etl.core.exception;

import org.apn.etl.core.validation.ValidationResult;

/**
 * Exception thrown when data validation fails
 *
 * @author Amit Prakash Nema
 */
public class ValidationException extends ETLException {

  private static final long serialVersionUID = 1L;
  private final ValidationResult validationResult;

  public ValidationException(
      final String message, final ValidationResult validationResult) {
    super(message);
    this.validationResult = validationResult;
  }

  public ValidationResult getValidationResult() {
    return validationResult;
  }
}
