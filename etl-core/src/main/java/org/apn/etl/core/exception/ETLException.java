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

/**
 * Base exception class for the ETL framework. This is the general exception thrown for errors
 * during the ETL process.
 *
 * @author Amit Prakash Nema
 */
public class ETLException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new ETLException with the specified detail message.
   *
   * @param message the detail message.
   */
  public ETLException(final String message) {
    super(message);
  }

  /**
   * Constructs a new ETLException with the specified detail message and cause.
   *
   * @param message the detail message.
   * @param cause the cause.
   */
  public ETLException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
