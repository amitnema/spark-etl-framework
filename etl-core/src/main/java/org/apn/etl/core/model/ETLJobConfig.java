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

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration model for ETL jobs in the framework.
 *
 * <p>Encapsulates job metadata, input/output configuration, transformation, validation, and
 * parameters.
 *
 * @author Amit Prakash Nema
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ETLJobConfig {
  private String jobName;
  private String jobDescription;
  private String jobVersion;
  private List<InputConfig> inputs;
  private TransformationConfig transformation;
  private List<OutputConfig> outputs;
  private ValidationConfig validation;
  private Map<String, Object> parameters;
}
