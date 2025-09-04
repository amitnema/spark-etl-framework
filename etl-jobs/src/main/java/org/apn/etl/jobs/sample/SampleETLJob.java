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

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apn.etl.core.ETLEngine;
import org.apn.etl.core.exception.ETLException;
import org.apn.etl.core.model.ETLJobConfig;
import org.apn.etl.core.utils.ETLUtils;

/**
 * Sample ETL Job implementation
 *
 * @author Amit Prakash Nema
 */
@Slf4j
public final class SampleETLJob {
  private SampleETLJob() {
    // private constructor to hide the implicit public one
  }

  /**
   * Entry point for the program.
   *
   * @param args the args
   */
  public static void main(final String[] args) {
    try {
      // Parse command line arguments
      final CommandLine cmd = parseArguments(args);

      // Load job configuration
      final String configPath = cmd.getOptionValue("config", "job-config.yaml");
      final ETLJobConfig jobConfig = loadJobConfig(configPath);

      // Override job name if provided
      if (cmd.hasOption("job-name")) {
        jobConfig.setJobName(cmd.getOptionValue("job-name"));
      }

      // Create and execute ETL engine
      final ETLEngine engine = new ETLEngine(jobConfig);
      engine.execute();

      log.info("ETL job execution completed successfully");

    } catch (final ParseException | IOException | ETLException e) {
      log.error("ETL job execution failed", e);
      System.exit(1);
    }
  }

  public static CommandLine parseArguments(final String[] args) throws ParseException {
    final Options options = new Options();

    options.addOption(
        Option.builder("c")
            .longOpt("config")
            .hasArg()
            .desc("Path to job configuration file")
            .build());

    options.addOption(
        Option.builder("n").longOpt("job-name").hasArg().desc("Job name override").build());

    options.addOption(Option.builder("h").longOpt("help").desc("Show help message").build());

    final CommandLineParser parser = new DefaultParser();
    final CommandLine cmd = parser.parse(options, args);

    if (cmd.hasOption("help")) {
      final HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("SampleETLJob", options);
      System.exit(0);
    }

    return cmd;
  }

  public static ETLJobConfig loadJobConfig(final String configPath) throws IOException {
    log.info("Loading job configuration from: {}", configPath);
    return ETLUtils.loadYamlConfig(configPath, ETLJobConfig.class);
  }
}
