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
import org.apache.spark.sql.SparkSession;
import org.apn.etl.core.config.SparkConfig;

/**
 * SQL-based data transformer for executing custom SQL queries on Spark datasets.
 *
 * <p>This transformer creates a temporary view from the input dataset and executes the provided SQL
 * query. <b>Parameters:</b>
 *
 * <ul>
 *   <li>sql - The SQL query to execute (required)
 *   <li>tempViewName - The name of the temporary view (optional, default: "input_data")
 * </ul>
 *
 * @author Amit Prakash Nema
 */
public final class SqlDataTransformer extends AbstractDataTransformer {
  /**
   * Executes the SQL transformation on the input dataset.
   *
   * @param input Input dataset
   * @param parameters Transformation parameters (must include "sql")
   * @return Transformed dataset after SQL execution
   * @throws IllegalArgumentException if SQL query is missing
   * @throws RuntimeException if SQL execution fails
   */
  @Override
  protected Dataset<Row> doTransform(
      final Dataset<Row> input, final Map<String, Object> parameters) {
    final SparkSession spark = SparkConfig.getSparkSession();

    final String sql = getParameter(parameters, "sql", "");
    final String tempViewName = getParameter(parameters, "tempViewName", "input_data");

    if (sql.isEmpty()) {
      throw new IllegalArgumentException("SQL query is required for SqlDataTransformer");
    }

    logger.info("Executing SQL transformation with temp view: {}", tempViewName);
    logger.debug("SQL Query: {}", sql);

    // Create temporary view
    input.createOrReplaceTempView(tempViewName);

    try {
      // Execute SQL query
      return spark.sql(sql);
    } catch (final Exception e) {
      logger.error("Error executing SQL query", e);
      throw new RuntimeException("SQL transformation failed", e);
    }
  }
}
