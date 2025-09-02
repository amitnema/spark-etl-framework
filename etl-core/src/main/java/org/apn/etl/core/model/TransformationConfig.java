package org.apn.etl.core.model;

import java.util.List;
import java.util.Map;

/**
 * Transformation configuration model for ETL jobs.
 * <p>
 * Defines the transformation logic, including transformer class, steps, parameters, SQL file, and custom functions.
 * </p>
 *
 * @author Amit Prakash Nema
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
    /**
     * Gets the transformer class name.
     * @return class name
     */
    public String getClassName() { return className; }
    /**
     * Sets the transformer class name.
     * @param className class name
     */
    public void setClassName(String className) { this.className = className; }

    /**
     * Gets the list of transformation steps.
     * @return list of steps
     */
    public List<String> getSteps() { return steps; }
    /**
     * Sets the list of transformation steps.
     * @param steps list of steps
     */
    public void setSteps(List<String> steps) { this.steps = steps; }

    /**
     * Gets the transformation parameters.
     * @return parameters map
     */
    public Map<String, Object> getParameters() { return parameters; }
    /**
     * Sets the transformation parameters.
     * @param parameters parameters map
     */
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    /**
     * Gets the SQL file for SQL-based transformations.
     * @return SQL file path
     */
    public String getSqlFile() { return sqlFile; }
    /**
     * Sets the SQL file for SQL-based transformations.
     * @param sqlFile SQL file path
     */
    public void setSqlFile(String sqlFile) { this.sqlFile = sqlFile; }

    /**
     * Gets the list of custom functions for transformation.
     * @return list of custom functions
     */
    public List<String> getCustomFunctions() { return customFunctions; }
    /**
     * Sets the list of custom functions for transformation.
     * @param customFunctions list of custom functions
     */
    public void setCustomFunctions(List<String> customFunctions) { this.customFunctions = customFunctions; }
}