package org.apn.etl.core.model;

import java.util.List;

/**
 * Schema configuration model
 */
public class SchemaConfig {
    private List<FieldConfig> fields;
    private boolean strictMode;

    public SchemaConfig() {}

    public List<FieldConfig> getFields() { return fields; }
    public void setFields(List<FieldConfig> fields) { this.fields = fields; }

    public boolean isStrictMode() { return strictMode; }
    public void setStrictMode(boolean strictMode) { this.strictMode = strictMode; }

    public static class FieldConfig {
        private String name;
        private String type;
        private boolean nullable;
        private Object defaultValue;

        public FieldConfig() {}

        public FieldConfig(String name, String type, boolean nullable) {
            this.name = name;
            this.type = type;
            this.nullable = nullable;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public boolean isNullable() { return nullable; }
        public void setNullable(boolean nullable) { this.nullable = nullable; }

        public Object getDefaultValue() { return defaultValue; }
        public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }
    }
}