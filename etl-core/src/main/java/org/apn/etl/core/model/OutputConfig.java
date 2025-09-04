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
package org.apn.etl.core.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Output configuration model for ETL jobs.
 *
 * <p>Defines the target output details including type, format, path, connection, mode, options, and
 * partitioning.
 *
 * @author Amit Prakash Nema
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputConfig {
  private String name;
  private String type; // file, database, stream, etc.
  private String format; // parquet, json, csv, etc.
  private String path;
  private String connectionString;
  private String mode; // overwrite, append, ignore, error
  private Map<String, Object> options = com.google.common.collect.Maps.newHashMap();
  private PartitionConfig partition;
}
