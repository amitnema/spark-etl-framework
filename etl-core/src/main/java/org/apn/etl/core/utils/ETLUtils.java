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
package org.apn.etl.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for common ETL operations.
 *
 * <p>Provides methods for loading YAML configs, converting objects to JSON, formatting dates, and
 * more.
 *
 * @author Amit Prakash Nema
 */
@UtilityClass
@Slf4j
public class ETLUtils {
  private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
  private final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Loads YAML configuration from the classpath and deserializes to the specified class.
   *
   * @param resourcePath path to YAML resource
   * @param clazz target class
   * @param <T> type of config
   * @return deserialized config object
   * @throws IOException if resource not found or parsing fails
   */
  public <T> T loadYamlConfig(final String resourcePath, final Class<T> clazz) throws IOException {
    try (var is =
        new File(resourcePath).exists()
            ? new FileInputStream(resourcePath)
            : ETLUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
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
  public Map<String, Object> loadYamlConfigAsMap(final String resourcePath) throws IOException {
    return loadYamlConfig(resourcePath, Map.class);
  }

  /**
   * Converts an object to a JSON string.
   *
   * @param object object to convert
   * @return JSON string
   */
  public String toJsonString(final Object object) {
    try {
      return jsonMapper.writeValueAsString(object);
    } catch (final Exception e) {
      log.error("Error converting object to JSON", e);
      return "{}";
    }
  }

  /** Generate timestamp string for file naming */
  public String getTimestampString() {
    return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
  }

  /** Generate timestamp string with custom format */
  public String getTimestampString(final String format) {
    return new SimpleDateFormat(format).format(new Date());
  }

  /** Safely get nested value from Map */
  @SuppressWarnings("unchecked")
  public <T> T getNestedValue(
      final Map<String, Object> map, final String path, final T defaultValue) {
    final String[] keys = path.split("\\.");
    Object current = map;

    for (final String key : keys) {
      if (current instanceof Map) {
        current = ((Map<String, Object>) current).get(key);
      } else {
        return defaultValue;
      }
    }

    try {
      return current != null ? (T) current : defaultValue;
    } catch (final ClassCastException e) {
      log.warn("Type cast error for path: {}, returning default value", path);
      return defaultValue;
    }
  }

  /** Check if string is null or empty */
  public boolean isEmpty(final String str) {
    return str == null || str.trim().isEmpty();
  }

  /** Check if string is not null and not empty */
  public boolean isNotEmpty(final String str) {
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
  public <K, V> Map<K, String> toStringMap(final Map<K, V> inputMap) {
    if (inputMap == null) {
      return Collections.emptyMap();
    }
    return inputMap.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
  }
}
