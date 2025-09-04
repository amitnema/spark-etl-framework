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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

/**
 * Spark session configuration and management.
 *
 * @author Amit Prakash Nema
 */
@UtilityClass
@Slf4j
public class SparkConfig {
  private SparkSession sparkSession;
  private final ConfigurationManager config = ConfigurationManager.getInstance();

  /**
   * Gets the singleton SparkSession instance, creating it if it doesn't exist.
   *
   * @return The SparkSession instance.
   */
  public synchronized SparkSession getSparkSession() {
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
  private SparkSession createSparkSession() {
    final var appName = config.getProperty("spark.app.name", "ETL-Framework");
    final var master = config.getProperty("spark.master", "local[*]");

    final var conf =
        new SparkConf()
            .setAppName(appName)
            .setMaster(master)
            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .set("spark.sql.adaptive.enabled", "true")
            .set("spark.sql.adaptive.coalescePartitions.enabled", "true")
            .set("spark.sql.adaptive.skewJoin.enabled", "true");

    // Add cloud-specific configurations
    addCloudConfiguration(conf);

    log.info("Creating Spark session with app name: {} and master: {}", appName, master);

    return SparkSession.builder().config(conf).getOrCreate();
  }

  /**
   * Adds cloud-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private void addCloudConfiguration(final SparkConf conf) {
    final var cloudProvider = config.getProperty("cloud.provider", "local");

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
        log.info("Using local configuration");
    }
  }

  /**
   * Adds AWS-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private void addAWSConfiguration(final SparkConf conf) {
    conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
    conf.set(
        "spark.hadoop.fs.s3a.aws.credentials.provider",
        "com.amazonaws.auth.DefaultAWSCredentialsProviderChain");

    final var accessKey = config.getProperty("aws.access.key");
    final var secretKey = config.getProperty("aws.secret.key");

    if (accessKey != null && secretKey != null) {
      conf.set("spark.hadoop.fs.s3a.access.key", accessKey);
      conf.set("spark.hadoop.fs.s3a.secret.key", secretKey);
    }

    log.info("AWS configuration applied");
  }

  /**
   * Adds GCP-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private void addGCPConfiguration(final SparkConf conf) {
    final var serviceAccountKey = config.getProperty("gcp.service.account.key");
    if (serviceAccountKey != null) {
      conf.set("spark.hadoop.google.cloud.auth.service.account.json.keyfile", serviceAccountKey);
    }

    conf.set("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem");
    conf.set(
        "spark.hadoop.fs.AbstractFileSystem.gs.impl",
        "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS");

    log.info("GCP configuration applied");
  }

  /**
   * Adds Azure-specific configurations to the SparkConf.
   *
   * @param conf The SparkConf to add the configuration to.
   */
  private void addAzureConfiguration(final SparkConf conf) {
    final var storageAccount = config.getProperty("azure.storage.account");
    final var accessKey = config.getProperty("azure.storage.access.key");

    if (storageAccount != null && accessKey != null) {
      conf.set(
          String.format(
              "spark.hadoop.fs.azure.account.key.%s.dfs.core.windows.net", storageAccount),
          accessKey);
    }

    log.info("Azure configuration applied");
  }

  /** Closes the existing SparkSession if it's running. */
  public void closeSparkSession() {
    if (sparkSession != null) {
      sparkSession.close();
      sparkSession = null;
    }
  }
}
