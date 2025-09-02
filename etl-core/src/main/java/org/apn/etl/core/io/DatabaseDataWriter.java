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
import org.apache.spark.sql.SaveMode;
import org.apn.etl.core.model.OutputConfig;

/**
 * Database data writer implementation.
 *
 * @author Amit Prakash Nema
 */
@Slf4j
public class DatabaseDataWriter implements DataWriter {

  /**
   * Writes a Spark Dataset to a database table.
   *
   * @param dataset The Dataset to write.
   * @param config The output configuration specifying connection details, table name, etc.
   * @throws IllegalArgumentException if the connection string or table name is missing.
   */
  @Override
  public void write(final Dataset<Row> dataset, final OutputConfig config) {
    final var connectionString = config.getConnectionString();
    final var tableName = config.getPath(); // Using path as table name
    final SaveMode mode = getSaveMode(config.getMode());

    if (connectionString == null || connectionString.isEmpty()) {
      throw new IllegalArgumentException("Connection string is required for database writer");
    }

    if (tableName == null || tableName.isEmpty()) {
      throw new IllegalArgumentException("Table name is required for database writer");
    }

    log.info("Writing to database table: {} with mode: {}", tableName, mode);

    final Properties connectionProps = new Properties();

    // Add connection properties from options
    if (config.getOptions() != null) {
      config
          .getOptions()
          .forEach((key, value) -> connectionProps.setProperty(key, String.valueOf(value)));
    }

    dataset.write().mode(mode).jdbc(connectionString, tableName, connectionProps);
  }

  /**
   * Converts a string representation of a save mode to the Spark SaveMode enum.
   *
   * @param mode The save mode as a string (e.g., "overwrite", "append").
   * @return The corresponding Spark SaveMode enum. Defaults to ErrorIfExists.
   */
  private SaveMode getSaveMode(final String mode) {
    if (mode == null) return SaveMode.ErrorIfExists;

    switch (mode.toLowerCase()) {
      case "overwrite":
        return SaveMode.Overwrite;
      case "append":
        return SaveMode.Append;
      case "ignore":
        return SaveMode.Ignore;
      case "error":
      default:
        return SaveMode.ErrorIfExists;
    }
  }
}
