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
public final class SampleDataTransformer extends AbstractDataTransformer {

  private static final String PROCESSED_TIMESTAMP_COL = "processed_timestamp";
  private static final String YEAR_COL = "year";
  private static final String MONTH_COL = "month";
  private static final String DATE_COLUMN_COL = "date_column";
  private static final String STATUS_COL = "status";
  private static final String ACTIVE_STATUS = "active";

  @Override
  protected Dataset<Row> doTransform(
      final Dataset<Row> input, final Map<String, Object> parameters) {
    logger.info("Executing sample data transformation");

    // Sample transformation: Add current timestamp and filter active records
    final Dataset<Row> transformed =
        input
            .withColumn(PROCESSED_TIMESTAMP_COL, functions.current_timestamp())
            .withColumn(YEAR_COL, functions.year(functions.col(DATE_COLUMN_COL)))
            .withColumn(MONTH_COL, functions.month(functions.col(DATE_COLUMN_COL)))
            .filter(functions.col(STATUS_COL).equalTo(ACTIVE_STATUS));

    logger.info(
        "Sample transformation completed. Input records: {}, Output records: {}",
        input.count(),
        transformed.count());

    return transformed;
  }
}
