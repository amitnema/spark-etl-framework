package org.apn.etl.core.model;

import java.util.List;
import java.util.Map;

/**
 * Validation configuration model for ETL jobs.
 * <p>
 * Defines validation rules, parameters, and failure handling strategy.
 * </p>
 *
 * @author Amit Prakash Nema
 */
public class ValidationConfig {
    private boolean enabled;
    private List<ValidationRule> rules;
    private String onFailure; // STOP, CONTINUE, QUARANTINE
    private Map<String, Object> parameters;

    public ValidationConfig() {}

    /**
     * Checks if validation is enabled.
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() { return enabled; }
    /**
     * Enables or disables validation.
     * @param enabled true to enable validation
     */
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    /**
     * Gets the list of validation rules.
     * @return list of validation rules
     */
    public List<ValidationRule> getRules() { return rules; }
    /**
     * Sets the list of validation rules.
     * @param rules list of validation rules
     */
    public void setRules(List<ValidationRule> rules) { this.rules = rules; }

    /**
     * Gets the failure handling strategy (STOP, CONTINUE, QUARANTINE).
     * @return failure strategy
     */
    public String getOnFailure() { return onFailure; }
    /**
     * Sets the failure handling strategy.
     * @param onFailure failure strategy
     */
    public void setOnFailure(String onFailure) { this.onFailure = onFailure; }

    /**
     * Gets additional validation parameters.
     * @return parameters map
     */
    public Map<String, Object> getParameters() { return parameters; }
    /**
     * Sets additional validation parameters.
     * @param parameters parameters map
     */
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    /**
     * Validation rule configuration for ETL jobs.
     * @author Amit Prakash Nema
     */
    public static class ValidationRule {
        private String name;
        private String type; // NOT_NULL, UNIQUE, RANGE, REGEX, CUSTOM
        private String column;
        private Map<String, Object> parameters;

        public ValidationRule() {}

        /**
         * Gets the rule name.
         * @return rule name
         */
        public String getName() { return name; }
        /**
         * Sets the rule name.
         * @param name rule name
         */
        public void setName(String name) { this.name = name; }

        /**
         * Gets the rule type (NOT_NULL, UNIQUE, RANGE, REGEX, CUSTOM).
         * @return rule type
         */
        public String getType() { return type; }
        /**
         * Sets the rule type.
         * @param type rule type
         */
        public void setType(String type) { this.type = type; }

        /**
         * Gets the column to validate.
         * @return column name
         */
        public String getColumn() { return column; }
        /**
         * Sets the column to validate.
         * @param column column name
         */
        public void setColumn(String column) { this.column = column; }

        /**
         * Gets additional rule parameters.
         * @return parameters map
         */
        public Map<String, Object> getParameters() { return parameters; }
        /**
         * Sets additional rule parameters.
         * @param parameters parameters map
         */
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }
}