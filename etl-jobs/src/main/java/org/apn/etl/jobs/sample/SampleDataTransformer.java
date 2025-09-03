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
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
* See the License for the specific language governing permissions and
* limitations under the License
*/
package org.apn.etl.jobs.sample;

import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apn.etl.core.transformation.AbstractDataTransformer;

/**
 * Sample data transformer implementation.
 *
 * @author Amit Prakash Nema
 */
public class SampleDataTransformer extends AbstractDataTransformer {

  @Override
  protected Dataset<Row> doTransform(Dataset<Row> input, Map<String, Object> parameters) {
    logger.info("Executing sample data transformation");

    // Sample transformation: Add current timestamp and filter active records
    Dataset<Row> transformed =
        input
            .withColumn("processed_timestamp", functions.current_timestamp())
            .withColumn("year", functions.year(functions.col("date_column")))
            .filter(functions.col("status").equalTo("active"));

    logger.info(
        "Sample transformation completed. Input records: {}, Output records: {}",
        input.count(),
        transformed.count());

    return transformed;
  }
}
