package org.apn.etl.core.model;

import java.util.List;
import java.util.Map;

/**
 * Configuration model for ETL jobs in the framework.
 * <p>
 * Encapsulates job metadata, input/output configuration, transformation, validation, and parameters.
 * </p>
 *
 * @author Amit Prakash Nema
 */
public class ETLJobConfig {
    private String jobName;
    private String jobDescription;
    private String jobVersion;
    private List<InputConfig> inputs;
    private TransformationConfig transformation;
    private List<OutputConfig> outputs;
    private ValidationConfig validation;
    private Map<String, Object> parameters;

    // Constructors
    public ETLJobConfig() {}

    public ETLJobConfig(String jobName, String jobDescription, String jobVersion) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobVersion = jobVersion;
    }

    // Getters and Setters
    /**
     * Gets the job name.
     * @return job name
     */
    public String getJobName() { return jobName; }
    /**
     * Sets the job name.
     * @param jobName job name
     */
    public void setJobName(String jobName) { this.jobName = jobName; }

    /**
     * Gets the job description.
     * @return job description
     */
    public String getJobDescription() { return jobDescription; }
    /**
     * Sets the job description.
     * @param jobDescription job description
     */
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    /**
     * Gets the job version.
     * @return job version
     */
    public String getJobVersion() { return jobVersion; }
    /**
     * Sets the job version.
     * @param jobVersion job version
     */
    public void setJobVersion(String jobVersion) { this.jobVersion = jobVersion; }

    /**
     * Gets the input configurations.
     * @return list of input configs
     */
    public List<InputConfig> getInputs() { return inputs; }
    /**
     * Sets the input configurations.
     * @param inputs list of input configs
     */
    public void setInputs(List<InputConfig> inputs) { this.inputs = inputs; }

    /**
     * Gets the transformation configuration.
     * @return transformation config
     */
    public TransformationConfig getTransformation() { return transformation; }
    /**
     * Sets the transformation configuration.
     * @param transformation transformation config
     */
    public void setTransformation(TransformationConfig transformation) { this.transformation = transformation; }

    /**
     * Gets the output configurations.
     * @return list of output configs
     */
    public List<OutputConfig> getOutputs() { return outputs; }
    /**
     * Sets the output configurations.
     * @param outputs list of output configs
     */
    public void setOutputs(List<OutputConfig> outputs) { this.outputs = outputs; }

    /**
     * Gets the validation configuration.
     * @return validation config
     */
    public ValidationConfig getValidation() { return validation; }
    /**
     * Sets the validation configuration.
     * @param validation validation config
     */
    public void setValidation(ValidationConfig validation) { this.validation = validation; }

    /**
     * Gets job parameters.
     * @return parameters map
     */
    public Map<String, Object> getParameters() { return parameters; }
    /**
     * Sets job parameters.
     * @param parameters parameters map
     */
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
}