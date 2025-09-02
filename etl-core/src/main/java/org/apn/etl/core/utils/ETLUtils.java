package org.apn.etl.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for common ETL operations
 */
public class ETLUtils {
    private static final Logger logger = LoggerFactory.getLogger(ETLUtils.class);
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Load YAML configuration from classpath
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
     * Load YAML configuration as Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadYamlConfigAsMap(String resourcePath) throws IOException {
        return loadYamlConfig(resourcePath, Map.class);
    }

    /**
     * Convert object to JSON string
     */
    public static String toJsonString(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Error converting object to JSON", e);
            return "{}";
        }
    }

    /**
     * Generate timestamp string for file naming
     */
    public static String getTimestampString() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    /**
     * Generate timestamp string with custom format
     */
    public static String getTimestampString(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * Safely get nested value from Map
     */
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

    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Converts a map to a new map where all values are strings.
     * @param inputMap The source map with any value type.
     * @param <K> The type of the keys in the map.
     * @param <V> The type of the values in the map.
     * @return A new map with the same keys and string representations of the values.
     */
    public static <K, V> Map<K, String> toStringMap(Map<K, V> inputMap) {
        return inputMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.valueOf(entry.getValue())
                ));
    }
}