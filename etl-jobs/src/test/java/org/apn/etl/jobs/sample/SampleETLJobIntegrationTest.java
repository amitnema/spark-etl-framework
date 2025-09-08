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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * The type Sample etl job integration test.
 *
 * @author Amit Prakash Nema
 */
@Slf4j
class SampleETLJobIntegrationTest {
  /**
   * Test etl job execution with json input.
   *
   * @throws Exception the exception
   */
  @Test
  void testETLJobExecutionWithJsonInput() throws Exception {
    String configPath = "jobs/json-job/job-config.yaml";
    String[] args = {"--config", configPath, "--job-name", "JsonJobTest"};
    String outputDir = SampleETLJob.loadJobConfig(configPath).getOutputs().get(0).getPath();
    cleanDir(outputDir);
    // Run the ETL job
    SampleETLJob.main(args);
    log.info("ETL job executed with config: {}", configPath);
    // Check that output files are created
    File[] outputFiles;
    try (var paths = Files.walk(Paths.get(outputDir))) {
      outputFiles =
          paths
              .filter(path -> path.toString().endsWith(".json"))
              .map(java.nio.file.Path::toFile)
              .toArray(File[]::new);
    }
    assertNotNull(outputFiles);
    assertTrue(outputFiles.length > 0, "Output JSON files should be created");
    // Optionally, validate that output json each line contains only 'active' records; json element
    // name is 'status'
    boolean foundActive = false;
    for (File file : outputFiles) {
      List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
      for (String line : lines) {
        assertTrue(
            line.contains("\"status\":\"active\""), "Each record should have status 'active'");
        if (line.contains("active")) {
          foundActive = true;
        }
      }
    }
    assertTrue(foundActive, "Output should contain 'active' records");
    log.info("Integration test for ETL job passed");
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void cleanDir(String outputDir) {
    // Clean output directory before test
    File outDir = new File(outputDir);
    if (outDir.exists()) {
      for (File file : Objects.requireNonNull(outDir.listFiles())) {
        file.delete();
      }
    } else {
      outDir.mkdirs();
    }
  }
}
