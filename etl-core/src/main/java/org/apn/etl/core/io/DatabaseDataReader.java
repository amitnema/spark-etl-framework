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
package org.apn.etl.core.io;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apn.etl.core.config.SparkConfig;
import org.apn.etl.core.model.InputConfig;

/**
 * Database data reader implementation.
 *
 * @author Amit Prakash Nema
 */
@Slf4j
public class DatabaseDataReader implements DataReader {

  /**
   * Reads data from a database source based on the provided configuration.
   *
   * @param config The input configuration for the database source.
   * @return A Spark Dataset containing the data from the database.
   * @throws IllegalArgumentException if the connection string is missing.
   */
  @Override
  public Dataset<Row> read(final InputConfig config) {
    final SparkSession spark = SparkConfig.getSparkSession();

    final var connectionString = config.getConnectionString();
    final var query = config.getQuery();

    if (connectionString == null || connectionString.isEmpty()) {
      throw new IllegalArgumentException("Connection string is required for database reader");
    }

    log.info("Reading from database: {}", connectionString);

    final Properties connectionProps = new Properties();

    // Add connection properties from options
    if (config.getOptions() != null) {
      config
          .getOptions()
          .forEach((key, value) -> connectionProps.setProperty(key, String.valueOf(value)));
    }

    if (query != null && !query.isEmpty()) {
      // Read with custom query
      return spark
          .read()
          .jdbc(connectionString, String.format("(%s) as query_table", query), connectionProps);
    } else {
      // Read entire table
      final var tableName = config.getPath(); // Using path as table name
      return spark.read().jdbc(connectionString, tableName, connectionProps);
    }
  }
}
