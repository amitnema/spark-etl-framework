package org.apn.etl.jobs.sample;

import org.apn.etl.core.ETLEngine;
import org.apn.etl.core.model.ETLJobConfig;
import org.apn.etl.core.utils.ETLUtils;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Sample ETL Job implementation
 */
public class SampleETLJob {
    private static final Logger logger = LoggerFactory.getLogger(SampleETLJob.class);

    public static void main(String[] args) {
        try {
            // Parse command line arguments
            CommandLine cmd = parseArguments(args);

            // Load job configuration
            String configPath = cmd.getOptionValue("config", "job-config.yaml");
            ETLJobConfig jobConfig = loadJobConfig(configPath);

            // Override job name if provided
            if (cmd.hasOption("job-name")) {
                jobConfig.setJobName(cmd.getOptionValue("job-name"));
            }

            // Create and execute ETL engine
            ETLEngine engine = new ETLEngine(jobConfig);
            engine.execute();

            logger.info("ETL job execution completed successfully");

        } catch (Exception e) {
            logger.error("ETL job execution failed", e);
            System.exit(1);
        }
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption(Option.builder("c")
            .longOpt("config")
            .hasArg()
            .desc("Path to job configuration file")
            .build());

        options.addOption(Option.builder("n")
            .longOpt("job-name")
            .hasArg()
            .desc("Job name override")
            .build());

        options.addOption(Option.builder("h")
            .longOpt("help")
            .desc("Show help message")
            .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("SampleETLJob", options);
            System.exit(0);
        }

        return cmd;
    }

    private static ETLJobConfig loadJobConfig(String configPath) throws IOException {
        logger.info("Loading job configuration from: {}", configPath);
        return ETLUtils.loadYamlConfig(configPath, ETLJobConfig.class);
    }
}