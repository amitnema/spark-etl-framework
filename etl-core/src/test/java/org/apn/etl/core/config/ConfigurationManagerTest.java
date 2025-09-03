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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ConfigurationManagerTest {

  private ConfigurationManager configManager;

  @BeforeEach
  void setUp() {
    configManager = ConfigurationManager.getInstance();
  }

  @Test
  void testSingletonPattern() {
    ConfigurationManager instance1 = ConfigurationManager.getInstance();
    ConfigurationManager instance2 = ConfigurationManager.getInstance();

    assertSame(instance1, instance2);
  }

  @Test
  void testPropertyOperations() {
    // Given
    String testKey = "test.property";
    String testValue = "test.value";

    // When
    configManager.setProperty(testKey, testValue);

    // Then
    assertEquals(testValue, configManager.getProperty(testKey));
  }

  @Test
  void testPropertyWithDefault() {
    // Given
    String nonExistentKey = "non.existent.key";
    String defaultValue = "default.value";

    // When
    String result = configManager.getProperty(nonExistentKey, defaultValue);

    // Then
    assertEquals(defaultValue, result);
  }

  @Test
  @Disabled
  void testJobConfigAccess() {
    // When
    Map<String, Object> jobConfig = configManager.getJobConfig();

    // Then
    // This test depends on whether test resources contain job config
    // In a real test, you'd mock or provide test configuration
    assertNotNull(jobConfig);
  }
}
