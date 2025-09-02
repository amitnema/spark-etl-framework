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

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apn.etl.core.model.OutputConfig;
import org.apn.etl.core.model.PartitionConfig;
import org.apn.etl.core.utils.ETLUtils;

/**
 * File-based data writer implementation.
 *
 * @author Amit Prakash Nema
 */
@Slf4j
public class FileDataWriter implements DataWriter {

  /**
   * Writes a Spark Dataset to a file-based destination.
   *
   * @param dataset The Dataset to write.
   * @param config The output configuration specifying format, path, save mode, etc.
   * @throws IllegalArgumentException if the file format in the config is not supported.
   */
  @Override
  public void write(final Dataset<Row> dataset, final OutputConfig config) {
    final var format = config.getFormat().toLowerCase();
    final var path = config.getPath();
    final SaveMode mode = getSaveMode(config.getMode());

    log.info("Writing {} file to path: {} with mode: {}", format, path, mode);

    DataFrameWriter<Row> writer =
        dataset.write().mode(mode).options(ETLUtils.toStringMap(config.getOptions()));

    // Apply partitioning if configured
    if (config.getPartition() != null) {
      writer = applyPartitioning(writer, config.getPartition());
    }

    switch (format) {
      case "parquet":
        writer.parquet(path);
        break;
      case "json":
        writer.json(path);
        break;
      case "csv":
        writer.option("header", "true").csv(path);
        break;
      case "avro":
        writer.format("avro").save(path);
        break;
      case "orc":
        writer.orc(path);
        break;
      default:
        throw new IllegalArgumentException("Unsupported file format: " + format);
    }
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

  /**
   * Applies partitioning to the DataFrameWriter based on the provided configuration.
   *
   * @param writer The DataFrameWriter to configure.
   * @param partition The partitioning configuration.
   * @return The configured DataFrameWriter.
   */
  private DataFrameWriter<Row> applyPartitioning(
      DataFrameWriter<Row> writer, final PartitionConfig partition) {
    if (partition.getColumns() != null && !partition.getColumns().isEmpty()) {
      return writer.partitionBy(partition.getColumns().toArray(new String[0]));
    }

    if (partition.getNumPartitions() > 0) {
      // This would need to be handled at dataset level before writing
      log.warn("Number of partitions should be set before calling write operation");
    }

    return writer;
  }
}
