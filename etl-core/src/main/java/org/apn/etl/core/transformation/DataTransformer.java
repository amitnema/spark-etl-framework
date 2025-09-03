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
package org.apn.etl.core.transformation;

import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Interface for data transformers in the ETL framework.
 *
 * <p>Implementations should provide logic to transform a Spark {@link Dataset} using provided
 * parameters.
 *
 * @author Amit Prakash Nema
 */
public interface DataTransformer {
  /**
   * Transforms the input dataset using the provided parameters.
   *
   * @param input Input dataset
   * @param parameters Transformation parameters
   * @return Transformed dataset
   */
  Dataset<Row> transform(Dataset<Row> input, Map<String, Object> parameters);
}
