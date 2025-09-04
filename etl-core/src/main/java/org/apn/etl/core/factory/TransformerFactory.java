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
package org.apn.etl.core.factory;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apn.etl.core.transformation.DataTransformer;
import org.apn.etl.core.transformation.SqlDataTransformer;

/**
 * Factory for creating data transformers. This class provides a static method to instantiate data
 * transformers, supporting both built-in and custom transformer implementations.
 *
 * @author Amit Prakash Nema
 */
@UtilityClass
@Slf4j
public class TransformerFactory {

  /**
   * Creates a {@link DataTransformer} instance based on the specified class name. It can
   * instantiate built-in transformers like "sql" or a custom transformer class by its fully
   * qualified name.
   *
   * @param className The name of the transformer to create (e.g., "sql") or the fully qualified
   *     class name of a custom transformer.
   * @return A {@link DataTransformer} instance.
   * @throws RuntimeException if the transformer class cannot be found, instantiated, or accessed.
   */
  public DataTransformer createTransformer(final String className) {
    log.info("Creating transformer: {}", className);

    try {
      // Handle built-in transformers
      switch (className.toLowerCase()) {
        case "sql":
        case "sqldatatransformer":
          return new SqlDataTransformer();
        default:
          // Try to load custom transformer class
          final var clazz = Class.forName(className);
          return (DataTransformer) clazz.getDeclaredConstructor().newInstance();
      }
    } catch (final Exception e) {
      log.error("Error creating transformer: {}", className, e);
      throw new RuntimeException("Failed to create transformer: " + className, e);
    }
  }
}
