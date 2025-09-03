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
package org.apn.etl.core.config;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spark session configuration and management.
 *
 * @author Amit Prakash Nema
 */
public final class SparkConfig {
  private static final Logger logger = LoggerFactory.getLogger(SparkConfig.class);

  private static SparkSession sparkSession;
  private static final ConfigurationManager config = ConfigurationManager.getInstance();

  private SparkConfig() {
    // private constructor to hide the implicit public one
  }

  /**
   * Gets the singleton SparkSession instance, creating it if it doesn't exist.
   *
   * @return The SparkSession instance.
   */
  public static synchronized SparkSession getSparkSession() {
    if (sparkSession == null) {
      sparkSession = createSparkSession();
    }
    return sparkSession;
  }

  /**
   * Creates a new SparkSession based on the application configuration.
   *
   * @return A new SparkSession.
   */
  private static SparkSession createSparkSession() {
    final String appName = config.getProperty("spark.app.name", "ETL-Framework");
    final String master = config.getProperty("spark.master", "local[*]");

    final SparkConf conf =
        new SparkConf()
            .setAppName(appName)
            .setMaster(master)
            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .set("spark.sql.adaptive.enabled", "true")
            .set("spark.sql.adaptive.coalescePartitions.enabled", "true")
            .set("spark.sql.adaptive.skewJoin.enabled", "true");

    // Add cloud-specific configurations
    addCloudConfiguration(conf);

    logger.info("Creating Spark session with app name: {} and master: {}", appName, master);

    return SparkSession.builder().config(conf).getOrCreate();
  }

  /**
   * Adds cloud-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private static void addCloudConfiguration(final SparkConf conf) {
    final String cloudProvider = config.getProperty("cloud.provider", "local");

    switch (cloudProvider.toLowerCase()) {
      case "aws":
        addAWSConfiguration(conf);
        break;
      case "gcp":
        addGCPConfiguration(conf);
        break;
      case "azure":
        addAzureConfiguration(conf);
        break;
      default:
        logger.info("Using local configuration");
    }
  }

  /**
   * Adds AWS-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private static void addAWSConfiguration(final SparkConf conf) {
    conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
    conf.set(
        "spark.hadoop.fs.s3a.aws.credentials.provider",
        "com.amazonaws.auth.DefaultAWSCredentialsProviderChain");

    final String accessKey = config.getProperty("aws.access.key");
    final String secretKey = config.getProperty("aws.secret.key");

    if (accessKey != null && secretKey != null) {
      conf.set("spark.hadoop.fs.s3a.access.key", accessKey);
      conf.set("spark.hadoop.fs.s3a.secret.key", secretKey);
    }

    logger.info("AWS configuration applied");
  }

  /**
   * Adds GCP-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private static void addGCPConfiguration(final SparkConf conf) {
    final String serviceAccountKey = config.getProperty("gcp.service.account.key");
    if (serviceAccountKey != null) {
      conf.set("spark.hadoop.google.cloud.auth.service.account.json.keyfile", serviceAccountKey);
    }

    conf.set("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem");
    conf.set(
        "spark.hadoop.fs.AbstractFileSystem.gs.impl",
        "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS");

    logger.info("GCP configuration applied");
  }

  /**
   * Adds Azure-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private static void addAzureConfiguration(final SparkConf conf) {
    final String storageAccount = config.getProperty("azure.storage.account");
    final String accessKey = config.getProperty("azure.storage.access.key");

    if (storageAccount != null && accessKey != null) {
      conf.set(
          String.format(
              "spark.hadoop.fs.azure.account.key.%s.dfs.core.windows.net", storageAccount),
          accessKey);
    }

    logger.info("Azure configuration applied");
  }

  /** Closes the existing SparkSession if it's running. */
  public static void closeSparkSession() {
    if (sparkSession != null) {
      sparkSession.close();
      sparkSession = null;
    }
  }
}
