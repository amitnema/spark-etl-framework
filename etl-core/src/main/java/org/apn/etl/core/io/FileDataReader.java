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
package org.apn.etl.core.io;

import static org.apn.etl.core.utils.ETLUtils.toStringMap;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apn.etl.core.config.SparkConfig;
import org.apn.etl.core.model.InputConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File-based data reader implementation.
 *
 * @author Amit Prakash Nema
 */
public class FileDataReader implements DataReader {
  private static final Logger logger = LoggerFactory.getLogger(FileDataReader.class);

  /**
   * Reads data from a file source based on the provided configuration.
   *
   * @param config The input configuration for the file source.
   * @return A Spark Dataset containing the data from the file.
   * @throws IllegalArgumentException if the file format in the config is not supported.
   */
  @Override
  public Dataset<Row> read(final InputConfig config) {
    final SparkSession spark = SparkConfig.getSparkSession();
    final String format = config.getFormat().toLowerCase();
    final String path = config.getPath();

    logger.info("Reading {} file from path: {}", format, path);

    switch (format) {
      case "parquet":
        return readParquet(spark, config);
      case "json":
        return readJson(spark, config);
      case "csv":
        return readCsv(spark, config);
      case "avro":
        return readAvro(spark, config);
      case "orc":
        return readOrc(spark, config);
      default:
        throw new IllegalArgumentException("Unsupported file format: " + format);
    }
  }

  private Dataset<Row> readParquet(final SparkSession spark, final InputConfig config) {
    return spark.read().options(toStringMap(config.getOptions())).parquet(config.getPath());
  }

  private Dataset<Row> readJson(final SparkSession spark, final InputConfig config) {
    return spark.read().options(toStringMap(config.getOptions())).json(config.getPath());
  }

  private Dataset<Row> readCsv(final SparkSession spark, final InputConfig config) {
    return spark
        .read()
        .option("header", "true")
        .option("inferSchema", "true")
        .options(toStringMap(config.getOptions()))
        .csv(config.getPath());
  }

  private Dataset<Row> readAvro(final SparkSession spark, final InputConfig config) {
    return spark
        .read()
        .format("avro")
        .options(toStringMap(config.getOptions()))
        .load(config.getPath());
  }

  private Dataset<Row> readOrc(final SparkSession spark, final InputConfig config) {
    return spark.read().options(toStringMap(config.getOptions())).orc(config.getPath());
  }
}
