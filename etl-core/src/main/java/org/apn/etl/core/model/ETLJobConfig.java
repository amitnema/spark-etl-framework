package org.apn.etl.core.model;

import java.util.List;
import java.util.Map;

/**
 * Configuration model for ETL jobs
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
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    public String getJobVersion() { return jobVersion; }
    public void setJobVersion(String jobVersion) { this.jobVersion = jobVersion; }

    public List<InputConfig> getInputs() { return inputs; }
    public void setInputs(List<InputConfig> inputs) { this.inputs = inputs; }

    public TransformationConfig getTransformation() { return transformation; }
    public void setTransformation(TransformationConfig transformation) { this.transformation = transformation; }

    public List<OutputConfig> getOutputs() { return outputs; }
    public void setOutputs(List<OutputConfig> outputs) { this.outputs = outputs; }

    public ValidationConfig getValidation() { return validation; }
    public void setValidation(ValidationConfig validation) { this.validation = validation; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
}