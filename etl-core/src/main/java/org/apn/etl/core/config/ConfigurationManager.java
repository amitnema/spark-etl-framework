package org.apn.etl.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Central configuration manager for ETL framework
 */
public class ConfigurationManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

    private static ConfigurationManager instance;
    private Properties properties;
    private Map<String, Object> jobConfig;

    private ConfigurationManager() {
        loadConfiguration();
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private void loadConfiguration() {
        // Load application.properties
        properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException e) {
            logger.error("Error loading application.properties", e);
        }

        // Load job configuration YAML
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("job-config.yaml")) {
            if (is != null) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                jobConfig = mapper.readValue(is, Map.class);
            }
        } catch (IOException e) {
            logger.error("Error loading job-config.yaml", e);
        }
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public Map<String, Object> getJobConfig() {
        return jobConfig;
    }

    @SuppressWarnings("unchecked")
    public <T> T getJobConfigValue(String path, Class<T> type) {
        String[] keys = path.split("\\.");
        Object current = jobConfig;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                return null;
            }
        }

        return type.cast(current);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}