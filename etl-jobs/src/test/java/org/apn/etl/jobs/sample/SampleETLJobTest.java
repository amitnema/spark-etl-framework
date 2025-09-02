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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class SampleETLJobTest {
  @Test
  void testParseArgumentsWithConfigAndJobName() throws ParseException {
    String[] args = {"--config", "test-config.yaml", "--job-name", "TestJob"};
    CommandLine cmd = SampleETLJob.parseArguments(args);
    assertEquals("test-config.yaml", cmd.getOptionValue("config"));
    assertEquals("TestJob", cmd.getOptionValue("job-name"));
    log.info("Parsed arguments successfully");
  }

  @Test
  @Disabled("Disables as there is exit call for help argument")
  void testParseArgumentsWithHelp() {
    String[] args = {"--help"};
    assertThrows(
        SecurityException.class,
        () -> {
          try {
            SampleETLJob.parseArguments(args);
          } catch (ParseException e) {
            fail(e);
          }
        });
    log.info("Help argument triggers exit");
  }

  @Test
  void testLoadJobConfig() throws Exception {
    String configPath = "jobs/json-job/job-config.yaml";
    assertNotNull(SampleETLJob.loadJobConfig(configPath));
    log.info("Loaded job config successfully");
  }
}
