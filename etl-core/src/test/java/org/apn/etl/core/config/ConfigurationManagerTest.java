package org.apn.etl.core.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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