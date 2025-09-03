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
package org.apn.etl.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for common ETL operations.
 *
 * <p>Provides methods for loading YAML configs, converting objects to JSON, formatting dates, and
 * more.
 *
 * @author Amit Prakash Nema
 */
public class ETLUtils {
  private static final Logger logger = LoggerFactory.getLogger(ETLUtils.class);
  private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
  private static final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Loads YAML configuration from the classpath and deserializes to the specified class.
   *
   * @param resourcePath path to YAML resource
   * @param clazz target class
   * @param <T> type of config
   * @return deserialized config object
   * @throws IOException if resource not found or parsing fails
   */
  public static <T> T loadYamlConfig(String resourcePath, Class<T> clazz) throws IOException {
    try (InputStream is = ETLUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }
      return yamlMapper.readValue(is, clazz);
    }
  }

  /**
   * Loads YAML configuration as a Map from the classpath.
   *
   * @param resourcePath path to YAML resource
   * @return config as Map
   * @throws IOException if resource not found or parsing fails
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> loadYamlConfigAsMap(String resourcePath) throws IOException {
    return loadYamlConfig(resourcePath, Map.class);
  }

  /**
   * Converts an object to a JSON string.
   *
   * @param object object to convert
   * @return JSON string
   */
  public static String toJsonString(Object object) {
    try {
      return jsonMapper.writeValueAsString(object);
    } catch (Exception e) {
      logger.error("Error converting object to JSON", e);
      return "{}";
    }
  }

  /** Generate timestamp string for file naming */
  public static String getTimestampString() {
    return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
  }

  /** Generate timestamp string with custom format */
  public static String getTimestampString(String format) {
    return new SimpleDateFormat(format).format(new Date());
  }

  /** Safely get nested value from Map */
  @SuppressWarnings("unchecked")
  public static <T> T getNestedValue(Map<String, Object> map, String path, T defaultValue) {
    String[] keys = path.split("\\.");
    Object current = map;

    for (String key : keys) {
      if (current instanceof Map) {
        current = ((Map<String, Object>) current).get(key);
      } else {
        return defaultValue;
      }
    }

    try {
      return current != null ? (T) current : defaultValue;
    } catch (ClassCastException e) {
      logger.warn("Type cast error for path: {}, returning default value", path);
      return defaultValue;
    }
  }

  /** Check if string is null or empty */
  public static boolean isEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  /** Check if string is not null and not empty */
  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  /**
   * Converts a map to a new map where all values are strings.
   *
   * @param inputMap The source map with any value type.
   * @param <K> The type of the keys in the map.
   * @param <V> The type of the values in the map.
   * @return A new map with the same keys and string representations of the values.
   */
  public static <K, V> Map<K, String> toStringMap(Map<K, V> inputMap) {
    return inputMap.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
  }
}
