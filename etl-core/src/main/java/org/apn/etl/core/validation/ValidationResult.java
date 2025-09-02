package org.apn.etl.core.validation;

import java.util.List;
import java.util.Map;

/**
 * Result of data validation
 */
public class ValidationResult {
    private boolean isValid;
    private List<ValidationError> errors;
    private Map<String, Object> metrics;
    private long recordsProcessed;
    private long recordsFailed;

    public ValidationResult() {}

    public ValidationResult(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }

    public List<ValidationError> getErrors() { return errors; }
    public void setErrors(List<ValidationError> errors) { this.errors = errors; }

    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }

    public long getRecordsProcessed() { return recordsProcessed; }
    public void setRecordsProcessed(long recordsProcessed) { this.recordsProcessed = recordsProcessed; }

    public long getRecordsFailed() { return recordsFailed; }
    public void setRecordsFailed(long recordsFailed) { this.recordsFailed = recordsFailed; }

    public static class ValidationError {
        private String ruleName;
        private String column;
        private String message;
        private Object value;

        public ValidationError() {}

        public ValidationError(String ruleName, String column, String message) {
            this.ruleName = ruleName;
            this.column = column;
            this.message = message;
        }

        // Getters and Setters
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }

        public String getColumn() { return column; }
        public void setColumn(String column) { this.column = column; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
    }
}