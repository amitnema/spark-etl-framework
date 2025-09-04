/*
* Copyright 2025 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License
*/
package org.apn.etl.core;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
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

/**
 * Main ETL processing engine
 *
 * @author Amit Prakash Nema
 */
@Slf4j
public final class ETLEngine {
  private final ETLJobConfig jobConfig;

  public ETLEngine(final ETLJobConfig jobConfig) {
    this.jobConfig = jobConfig;
  }

  /**
   * Execute the ETL job
   *
   * @throws ETLException if the job fails
   */
  public void execute() throws ETLException {
    log.info("Starting ETL job: {}", jobConfig.getJobName());

    try {
      // Step 1: Read input data
      final Map<String, Dataset<Row>> inputDatasets = readInputData();

      // Step 2: Validate input data
      validateInputData(inputDatasets);

      // Step 3: Transform data
      final Dataset<Row> transformedData = transformData(inputDatasets);

      // Step 4: Validate transformed data
      validateTransformedData(transformedData);

      // Step 5: Write output data
      writeOutputData(transformedData);

      log.info("ETL job completed successfully: {}", jobConfig.getJobName());

    } catch (final Exception e) {
      log.error("ETL job failed: {}", jobConfig.getJobName(), e);
      throw new ETLException("ETL job execution failed", e);
    } finally {
      // Cleanup resources
      cleanup();
    }
  }

  private Map<String, Dataset<Row>> readInputData() throws ETLException {
    log.info("Reading input data for {} input sources", jobConfig.getInputs().size());

    final Map<String, Dataset<Row>> datasets = new HashMap<>();

    for (final InputConfig inputConfig : jobConfig.getInputs()) {
      try {
        log.info("Reading input: {}", inputConfig.getName());

        final DataReader reader = IOFactory.createReader(inputConfig.getType());
        final Dataset<Row> dataset = reader.read(inputConfig);

        // Cache dataset for performance
        dataset.cache();

        datasets.put(inputConfig.getName(), dataset);

        log.info(
            "Successfully read input: {} with {} records", inputConfig.getName(), dataset.count());

      } catch (final Exception e) {
        throw new ETLException("Failed to read input: " + inputConfig.getName(), e);
      }
    }

    return datasets;
  }

  private void validateInputData(final Map<String, Dataset<Row>> datasets)
      throws ValidationException {
    if (jobConfig.getValidation() == null || !jobConfig.getValidation().isEnabled()) {
      log.info("Input validation is disabled, skipping validation");
      return;
    }

    log.info("Validating input data");

    for (final Map.Entry<String, Dataset<Row>> entry : datasets.entrySet()) {
      final DefaultDataValidator validator = new DefaultDataValidator(jobConfig.getValidation());
      final ValidationResult result = validator.validate(entry.getValue());

      if (!result.isValid()) {
        log.error("Input validation failed for: {}", entry.getKey());
        throw new ValidationException("Input validation failed", result);
      }

      log.info("Input validation passed for: {}", entry.getKey());
    }
  }

  private Dataset<Row> transformData(final Map<String, Dataset<Row>> inputDatasets)
      throws ETLException {
    log.info("Starting data transformation");

    try {
      final String transformerClass = jobConfig.getTransformation().getClassName();
      final DataTransformer transformer = TransformerFactory.createTransformer(transformerClass);

      // For multiple inputs, we'll use the first one as primary
      // In a more complex scenario, you might need custom logic
      final Dataset<Row> primaryDataset = inputDatasets.values().iterator().next();

      final Map<String, Object> parameters =
          jobConfig.getTransformation().getParameters() == null
              ? new HashMap<>()
              : new HashMap<>(jobConfig.getTransformation().getParameters());

      // Add input datasets to parameters for complex transformations
      parameters.put("inputDatasets", inputDatasets);

      final Dataset<Row> transformed = transformer.transform(primaryDataset, parameters);

      log.info("Data transformation completed successfully");
      return transformed;

    } catch (final Exception e) {
      throw new ETLException("Data transformation failed", e);
    }
  }

  private void validateTransformedData(final Dataset<Row> dataset) throws ValidationException {
    if (jobConfig.getValidation() == null || !jobConfig.getValidation().isEnabled()) {
      log.info("Output validation is disabled, skipping validation");
      return;
    }

    log.info("Validating transformed data");

    final DefaultDataValidator validator = new DefaultDataValidator(jobConfig.getValidation());
    final ValidationResult result = validator.validate(dataset);

    if (!result.isValid()) {
      log.error("Output validation failed");
      throw new ValidationException("Output validation failed", result);
    }

    log.info("Output validation passed");
  }

  private void writeOutputData(final Dataset<Row> dataset) throws ETLException {
    log.info("Writing output data to {} destinations", jobConfig.getOutputs().size());

    for (final OutputConfig outputConfig : jobConfig.getOutputs()) {
      try {
        log.info("Writing output: {}", outputConfig.getName());

        final DataWriter writer = IOFactory.createWriter(outputConfig.getType());
        writer.write(dataset, outputConfig);

        log.info("Successfully wrote output: {}", outputConfig.getName());

      } catch (final Exception e) {
        throw new ETLException("Failed to write output: " + outputConfig.getName(), e);
      }
    }
  }

  private void cleanup() {
    log.info("Cleaning up resources");
    SparkConfig.closeSparkSession();
  }
}
