package org.apn.etl.core.model;

import java.util.List;
import java.util.Map;

/**
 * Validation configuration model
 */
public class ValidationConfig {
    private boolean enabled;
    private List<ValidationRule> rules;
    private String onFailure; // STOP, CONTINUE, QUARANTINE
    private Map<String, Object> parameters;

    public ValidationConfig() {}

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public List<ValidationRule> getRules() { return rules; }
    public void setRules(List<ValidationRule> rules) { this.rules = rules; }

    public String getOnFailure() { return onFailure; }
    public void setOnFailure(String onFailure) { this.onFailure = onFailure; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    public static class ValidationRule {
        private String name;
        private String type; // NOT_NULL, UNIQUE, RANGE, REGEX, CUSTOM
        private String column;
        private Map<String, Object> parameters;

        public ValidationRule() {}

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getColumn() { return column; }
        public void setColumn(String column) { this.column = column; }

        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }
}