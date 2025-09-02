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
package org.apn.etl.core.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Central configuration manager for the ETL framework. This class handles loading configuration
 * from `application.properties` and `job-config.yaml`. It follows the singleton pattern to ensure a
 * single point of access to configuration.
 *
 * @author Amit Prakash Nema
 */
@Slf4j
@Getter
public final class ConfigurationManager {
  private static ConfigurationManager instance;
  private Properties properties;
  private Map<String, Object> jobConfig;

  /**
   * Private constructor to enforce the singleton pattern. Initializes the configuration by calling
   * {@link #loadConfiguration()}.
   */
  private ConfigurationManager() {
    loadConfiguration();
  }

  /**
   * Gets the singleton instance of the ConfigurationManager.
   *
   * @return The singleton ConfigurationManager instance.
   */
  public static synchronized ConfigurationManager getInstance() {
    if (instance == null) {
      instance = new ConfigurationManager();
    }
    return instance;
  }

  /** Loads configuration from `application.properties` and `job-config.yaml` from the classpath. */
  private void loadConfiguration() {
    // Load application.properties
    properties = new Properties();
    try (var is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
      if (is != null) {
        Objects.requireNonNull(properties).load(is);
      }
    } catch (final IOException e) {
      log.error("Error loading application.properties", e);
    }

    // Load job configuration YAML
    try (var is = getClass().getClassLoader().getResourceAsStream("job-config.yaml")) {
      if (is != null) {
        final var mapper = new ObjectMapper(new YAMLFactory());
        jobConfig = mapper.readValue(is, new TypeReference<>() {});
      }
    } catch (final IOException e) {
      log.error("Error loading job-config.yaml", e);
    }
  }

  /**
   * Gets a property value from the loaded `application.properties`.
   *
   * @param key The property key.
   * @return The property value, or null if not found.
   */
  public String getProperty(final String key) {
    return getProperty(key, null);
  }

  /**
   * Gets a property value from the loaded `application.properties`.
   *
   * @param key The property key.
   * @param defaultValue The default value to return if the key is not found.
   * @return The property value, or the default value if not found.
   */
  public String getProperty(final String key, final String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  /**
   * Gets a specific value from the job configuration using a dot-separated path.
   *
   * @param path The dot-separated path to the desired value (e.g., "job.name").
   * @param type The expected type of the value.
   * @param <T> The generic type of the value.
   * @return The configuration value cast to the specified type, or null if not found.
   */
  @SuppressWarnings("unchecked")
  public <T> T getJobConfigValue(final String path, final Class<T> type) {
    final String[] keys = path.split("\\.");
    Object current = jobConfig;

    for (final String key : keys) {
      if (current instanceof Map) {
        current = ((Map<String, Object>) current).get(key);
      } else {
        return null;
      }
    }

    return type.cast(current);
  }

  /**
   * Sets a property value. This will only affect the in-memory properties for the current run.
   *
   * @param key The property key to set.
   * @param value The property value to set.
   */
  public void setProperty(final String key, final String value) {
    properties.setProperty(key, value);
  }
}
