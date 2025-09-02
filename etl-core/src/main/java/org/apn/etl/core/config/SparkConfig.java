package org.apn.etl.core.config;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spark session configuration and management
 */
public class SparkConfig {
    private static final Logger logger = LoggerFactory.getLogger(SparkConfig.class);

    private static SparkSession sparkSession;
    private static final ConfigurationManager config = ConfigurationManager.getInstance();

    public static synchronized SparkSession getSparkSession() {
        if (sparkSession == null) {
            sparkSession = createSparkSession();
        }
        return sparkSession;
    }

    private static SparkSession createSparkSession() {
        String appName = config.getProperty("spark.app.name", "ETL-Framework");
        String master = config.getProperty("spark.master", "local[*]");

        SparkConf conf = new SparkConf()
            .setAppName(appName)
            .setMaster(master)
            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .set("spark.sql.adaptive.enabled", "true")
            .set("spark.sql.adaptive.coalescePartitions.enabled", "true")
            .set("spark.sql.adaptive.skewJoin.enabled", "true");

        // Add cloud-specific configurations
        addCloudConfiguration(conf);

        logger.info("Creating Spark session with app name: {} and master: {}", appName, master);

        return SparkSession.builder()
            .config(conf)
            .getOrCreate();
    }

    private static void addCloudConfiguration(SparkConf conf) {
        String cloudProvider = config.getProperty("cloud.provider", "local");

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

    private static void addAWSConfiguration(SparkConf conf) {
        conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        conf.set("spark.hadoop.fs.s3a.aws.credentials.provider", 
            "com.amazonaws.auth.DefaultAWSCredentialsProviderChain");

        String accessKey = config.getProperty("aws.access.key");
        String secretKey = config.getProperty("aws.secret.key");

        if (accessKey != null && secretKey != null) {
            conf.set("spark.hadoop.fs.s3a.access.key", accessKey);
            conf.set("spark.hadoop.fs.s3a.secret.key", secretKey);
        }

        logger.info("AWS configuration applied");
    }

    private static void addGCPConfiguration(SparkConf conf) {
        String serviceAccountKey = config.getProperty("gcp.service.account.key");
        if (serviceAccountKey != null) {
            conf.set("spark.hadoop.google.cloud.auth.service.account.json.keyfile", serviceAccountKey);
        }

        conf.set("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem");
        conf.set("spark.hadoop.fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS");

        logger.info("GCP configuration applied");
    }

    private static void addAzureConfiguration(SparkConf conf) {
        String storageAccount = config.getProperty("azure.storage.account");
        String accessKey = config.getProperty("azure.storage.access.key");

        if (storageAccount != null && accessKey != null) {
            conf.set(String.format("spark.hadoop.fs.azure.account.key.%s.dfs.core.windows.net", storageAccount), accessKey);
        }

        logger.info("Azure configuration applied");
    }

    public static void closeSparkSession() {
        if (sparkSession != null) {
            sparkSession.close();
            sparkSession = null;
        }
    }
}