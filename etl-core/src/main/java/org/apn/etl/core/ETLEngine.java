package org.apn.etl.core;

import org.apn.etl.core.config.ConfigurationManager;
import org.apn.etl.core.config.SparkConfig;
import org.apn.etl.core.exception.ETLException;
import org.apn.etl.core.exception.ValidationException;
import org.apn.etl.core.factory.IOFactory;
import org.apn.etl.core.factory.TransformerFactory;
import org.apn.etl.core.io.DataReader;
import org.apn.etl.core.io.DataWriter;
import org.apn.etl.core.model.ETLJobConfig;
import org.apn.etl.core.model.InputConfig;
import org.apn.etl.core.model.OutputConfig;
import org.apn.etl.core.transformation.DataTransformer;
import org.apn.etl.core.validation.DefaultDataValidator;
import org.apn.etl.core.validation.ValidationResult;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Main ETL processing engine
 */
public class ETLEngine {
    private static final Logger logger = LoggerFactory.getLogger(ETLEngine.class);

    private final ETLJobConfig jobConfig;
    private final ConfigurationManager configManager;

    public ETLEngine(ETLJobConfig jobConfig) {
        this.jobConfig = jobConfig;
        this.configManager = ConfigurationManager.getInstance();
    }

    /**
     * Execute the ETL job
     */
    public void execute() throws ETLException {
        logger.info("Starting ETL job: {}", jobConfig.getJobName());

        try {
            // Step 1: Read input data
            Map<String, Dataset<Row>> inputDatasets = readInputData();

            // Step 2: Validate input data
            validateInputData(inputDatasets);

            // Step 3: Transform data
            Dataset<Row> transformedData = transformData(inputDatasets);

            // Step 4: Validate transformed data
            validateTransformedData(transformedData);

            // Step 5: Write output data
            writeOutputData(transformedData);

            logger.info("ETL job completed successfully: {}", jobConfig.getJobName());

        } catch (Exception e) {
            logger.error("ETL job failed: {}", jobConfig.getJobName(), e);
            throw new ETLException("ETL job execution failed", e);
        } finally {
            // Cleanup resources
            cleanup();
        }
    }

    private Map<String, Dataset<Row>> readInputData() throws ETLException {
        logger.info("Reading input data for {} input sources", jobConfig.getInputs().size());

        Map<String, Dataset<Row>> datasets = new HashMap<>();

        for (InputConfig inputConfig : jobConfig.getInputs()) {
            try {
                logger.info("Reading input: {}", inputConfig.getName());

                DataReader reader = IOFactory.createReader(inputConfig.getType());
                Dataset<Row> dataset = reader.read(inputConfig);

                // Cache dataset for performance
                dataset.cache();

                datasets.put(inputConfig.getName(), dataset);

                logger.info("Successfully read input: {} with {} records", 
                    inputConfig.getName(), dataset.count());

            } catch (Exception e) {
                throw new ETLException("Failed to read input: " + inputConfig.getName(), e);
            }
        }

        return datasets;
    }

    private void validateInputData(Map<String, Dataset<Row>> datasets) throws ValidationException {
        if (jobConfig.getValidation() == null || !jobConfig.getValidation().isEnabled()) {
            logger.info("Input validation is disabled, skipping validation");
            return;
        }

        logger.info("Validating input data");

        for (Map.Entry<String, Dataset<Row>> entry : datasets.entrySet()) {
            DefaultDataValidator validator = new DefaultDataValidator(jobConfig.getValidation());
            ValidationResult result = validator.validate(entry.getValue());

            if (!result.isValid()) {
                logger.error("Input validation failed for: {}", entry.getKey());
                throw new ValidationException("Input validation failed", result);
            }

            logger.info("Input validation passed for: {}", entry.getKey());
        }
    }

    private Dataset<Row> transformData(Map<String, Dataset<Row>> inputDatasets) throws ETLException {
        logger.info("Starting data transformation");

        try {
            String transformerClass = jobConfig.getTransformation().getClassName();
            DataTransformer transformer = TransformerFactory.createTransformer(transformerClass);

            // For multiple inputs, we'll use the first one as primary
            // In a more complex scenario, you might need custom logic
            Dataset<Row> primaryDataset = inputDatasets.values().iterator().next();

            Map<String, Object> parameters = jobConfig.getTransformation().getParameters();
            if (parameters == null) {
                parameters = new HashMap<>();
            }

            // Add input datasets to parameters for complex transformations
            parameters.put("inputDatasets", inputDatasets);

            Dataset<Row> transformed = transformer.transform(primaryDataset, parameters);

            logger.info("Data transformation completed successfully");
            return transformed;

        } catch (Exception e) {
            throw new ETLException("Data transformation failed", e);
        }
    }

    private void validateTransformedData(Dataset<Row> dataset) throws ValidationException {
        if (jobConfig.getValidation() == null || !jobConfig.getValidation().isEnabled()) {
            logger.info("Output validation is disabled, skipping validation");
            return;
        }

        logger.info("Validating transformed data");

        DefaultDataValidator validator = new DefaultDataValidator(jobConfig.getValidation());
        ValidationResult result = validator.validate(dataset);

        if (!result.isValid()) {
            logger.error("Output validation failed");
            throw new ValidationException("Output validation failed", result);
        }

        logger.info("Output validation passed");
    }

    private void writeOutputData(Dataset<Row> dataset) throws ETLException {
        logger.info("Writing output data to {} destinations", jobConfig.getOutputs().size());

        for (OutputConfig outputConfig : jobConfig.getOutputs()) {
            try {
                logger.info("Writing output: {}", outputConfig.getName());

                DataWriter writer = IOFactory.createWriter(outputConfig.getType());
                writer.write(dataset, outputConfig);

                logger.info("Successfully wrote output: {}", outputConfig.getName());

            } catch (Exception e) {
                throw new ETLException("Failed to write output: " + outputConfig.getName(), e);
            }
        }
    }

    private void cleanup() {
        logger.info("Cleaning up resources");
        SparkConfig.closeSparkSession();
    }
}