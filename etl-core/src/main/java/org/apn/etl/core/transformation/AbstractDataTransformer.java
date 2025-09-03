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
package org.apn.etl.core.transformation;

import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for data transformers in the ETL framework.
 *
 * <p>Provides a template method pattern for transformation steps, including pre-transform and
 * post-transform hooks. Subclasses must implement the {@link #doTransform(Dataset, Map)} method to
 * provide custom transformation logic.
 *
 * @author Amit Prakash Nema
 */
public abstract class AbstractDataTransformer implements DataTransformer {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public final Dataset<Row> transform(
      final Dataset<Row> input, final Map<String, Object> parameters) {
    logger.info("Starting transformation: {}", getClass().getSimpleName());

    // Pre-transformation hook
    preTransform(input, parameters);

    // Main transformation logic
    final Dataset<Row> result = doTransform(input, parameters);

    // Post-transformation hook
    postTransform(result, parameters);

    logger.info("Transformation completed: {}", getClass().getSimpleName());
    return result;
  }

  /**
   * Pre-transformation hook for setup operations. Override to implement custom setup logic before
   * transformation.
   *
   * @param input Input dataset
   * @param parameters Transformation parameters
   */
  protected void preTransform(final Dataset<Row> input, final Map<String, Object> parameters) {
    // Default implementation does nothing
    logger.debug("Pre-transformation hook executed");
  }

  /**
   * Main transformation logic - must be implemented by subclasses.
   *
   * @param input Input dataset
   * @param parameters Transformation parameters
   * @return Transformed dataset
   */
  protected abstract Dataset<Row> doTransform(Dataset<Row> input, Map<String, Object> parameters);

  /**
   * Post-transformation hook for cleanup operations. Override to implement custom cleanup logic
   * after transformation.
   *
   * @param output Output dataset
   * @param parameters Transformation parameters
   */
  protected void postTransform(final Dataset<Row> output, final Map<String, Object> parameters) {
    // Default implementation does nothing
    logger.debug("Post-transformation hook executed");
  }

  /** Utility method to get parameter value with default */
  protected <T> T getParameter(
      final Map<String, Object> parameters, final String key, final T defaultValue) {
    final Object value = parameters.get(key);
    if (value == null) {
      return defaultValue;
    }

    try {
      return (T) value;
    } catch (final ClassCastException e) {
      logger.warn("Parameter {} has wrong type, using default value", key);
      return defaultValue;
    }
  }
}
