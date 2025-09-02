package org.apn.etl.core.model;

import java.util.List;
import java.util.Map;

/**
 * Transformation configuration model
 */
public class TransformationConfig {
    private String className;
    private List<String> steps;
    private Map<String, Object> parameters;
    private String sqlFile;
    private List<String> customFunctions;

    // Constructors
    public TransformationConfig() {}

    public TransformationConfig(String className) {
        this.className = className;
    }

    // Getters and Setters
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    public String getSqlFile() { return sqlFile; }
    public void setSqlFile(String sqlFile) { this.sqlFile = sqlFile; }

    public List<String> getCustomFunctions() { return customFunctions; }
    public void setCustomFunctions(List<String> customFunctions) { this.customFunctions = customFunctions; }
}