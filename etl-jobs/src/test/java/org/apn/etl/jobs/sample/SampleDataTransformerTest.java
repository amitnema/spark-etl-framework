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
package org.apn.etl.jobs.sample;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Lists;
import java.util.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** The type Sample data transformer test. */
class SampleDataTransformerTest {
  private static SparkSession spark;

  /** Sets up. */
  @BeforeAll
  static void setUp() {
    spark =
        SparkSession.builder()
            .master("local[1]")
            .appName("SampleDataTransformerTest")
            .getOrCreate();
  }

  /** Tear down. */
  @AfterAll
  static void tearDown() {
    if (spark != null) {
      spark.stop();
    }
  }

  private StructType getSchema() {
    return new StructType(
        new StructField[] {
          DataTypes.createStructField("id", DataTypes.IntegerType, false),
          DataTypes.createStructField("name", DataTypes.StringType, false),
          DataTypes.createStructField("age", DataTypes.IntegerType, false),
          DataTypes.createStructField("city", DataTypes.StringType, false),
          DataTypes.createStructField("date_column", DataTypes.StringType, false),
          DataTypes.createStructField("status", DataTypes.StringType, false)
        });
  }

  /** Test transform valid data. */
  @Test
  void testTransformValidData() {
    List<Row> rows =
        Arrays.asList(
            RowFactory.create(1, "Alice", 25, "Stuttgart", "2024-06-20T12:00:00Z", "active"),
            RowFactory.create(2, "Bob", 30, "Frankfurt", "2024-06-20T12:00:00Z", "active"),
            RowFactory.create(3, "Charlie", 35, "Berlin", "2024-06-20T12:00:00Z", "inactive"));
    Dataset<Row> df = spark.createDataFrame(rows, getSchema());
    SampleDataTransformer transformer = new SampleDataTransformer();
    Map<String, Object> params = new HashMap<>();
    Dataset<Row> result = transformer.transform(df, params);
    assertEquals(2, result.count()); // Only 'active' rows
    assertTrue(result.columns().length >= 8); // New columns added
    assertTrue(Arrays.asList(result.columns()).contains("year"));
    assertTrue(Arrays.asList(result.columns()).contains("month"));
    assertTrue(Arrays.asList(result.columns()).contains("processed_timestamp"));
  }

  /** Test transform empty data. */
  @Test
  void testTransformEmptyData() {
    Dataset<Row> df = spark.createDataFrame(Lists.newArrayList(), getSchema());
    SampleDataTransformer transformer = new SampleDataTransformer();
    Map<String, Object> params = new HashMap<>();
    Dataset<Row> result = transformer.transform(df, params);
    assertEquals(0, result.count());
  }
}
