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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AbstractDataTransformerTest {

  private static SparkSession spark;

  @BeforeAll
  static void setUp() {
    spark = SparkSession.builder().appName("TransformerTest").master("local[1]").getOrCreate();
    spark.sparkContext().setLogLevel("WARN");
  }

  @AfterAll
  static void tearDown() {
    if (spark != null) {
      spark.close();
    }
  }

  @Test
  void testTransformMethodExecution() {
    // Given
    TestTransformer transformer = new TestTransformer();
    Dataset<Row> mockInput = mock(Dataset.class);
    Dataset<Row> mockOutput = mock(Dataset.class);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("testParam", "testValue");

    // When
    transformer.setMockOutput(mockOutput);
    Dataset<Row> result = transformer.transform(mockInput, parameters);

    // Then
    assertEquals(mockOutput, result);
    assertTrue(transformer.preTransformCalled);
    assertTrue(transformer.postTransformCalled);
    assertTrue(transformer.doTransformCalled);
  }

  @Test
  void testGetParameterWithDefault() {
    // Given
    TestTransformer transformer = new TestTransformer();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("existingKey", "existingValue");

    // When & Then
    assertEquals(
        "existingValue", transformer.getParameterPublic(parameters, "existingKey", "default"));
    assertEquals(
        "default", transformer.getParameterPublic(parameters, "nonExistentKey", "default"));
  }

  // Test implementation
  private static class TestTransformer extends AbstractDataTransformer {
    boolean preTransformCalled = false;
    boolean postTransformCalled = false;
    boolean doTransformCalled = false;
    Dataset<Row> mockOutput;

    void setMockOutput(Dataset<Row> output) {
      this.mockOutput = output;
    }

    @Override
    protected void preTransform(Dataset<Row> input, Map<String, Object> parameters) {
      preTransformCalled = true;
    }

    @Override
    protected Dataset<Row> doTransform(Dataset<Row> input, Map<String, Object> parameters) {
      doTransformCalled = true;
      return mockOutput;
    }

    @Override
    protected void postTransform(Dataset<Row> output, Map<String, Object> parameters) {
      postTransformCalled = true;
    }

    // Public wrapper for testing protected method
    public <T> T getParameterPublic(Map<String, Object> parameters, String key, T defaultValue) {
      return getParameter(parameters, key, defaultValue);
    }
  }
}
