#!/bin/bash

# Spark ETL Framework - Job Submission Script
# This script submits ETL jobs to Spark cluster

set -e

# Default values
DEFAULT_MASTER="local[*]"
DEFAULT_DRIVER_MEMORY="2g"
DEFAULT_EXECUTOR_MEMORY="2g"
DEFAULT_EXECUTOR_CORES="2"
DEFAULT_NUM_EXECUTORS="2"

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JOB_DIR="${SCRIPT_DIR}"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --master)
            SPARK_MASTER="$2"
            shift 2
            ;;
        --driver-memory)
            DRIVER_MEMORY="$2"
            shift 2
            ;;
        --executor-memory)
            EXECUTOR_MEMORY="$2"
            shift 2
            ;;
        --executor-cores)
            EXECUTOR_CORES="$2"
            shift 2
            ;;
        --num-executors)
            NUM_EXECUTORS="$2"
            shift 2
            ;;
        --job-name)
            JOB_NAME="$2"
            shift 2
            ;;
        --config)
            CONFIG_FILE="$2"
            shift 2
            ;;
        --env)
            ENVIRONMENT="$2"
            shift 2
            ;;
        --help|-h)
            echo "Usage: $0 [OPTIONS]"
            echo "Options:"
            echo "  --master MASTER              Spark master URL (default: $DEFAULT_MASTER)"
            echo "  --driver-memory MEMORY       Driver memory (default: $DEFAULT_DRIVER_MEMORY)"
            echo "  --executor-memory MEMORY     Executor memory (default: $DEFAULT_EXECUTOR_MEMORY)"
            echo "  --executor-cores CORES       Executor cores (default: $DEFAULT_EXECUTOR_CORES)"
            echo "  --num-executors NUM          Number of executors (default: $DEFAULT_NUM_EXECUTORS)"
            echo "  --job-name NAME              Job name override"
            echo "  --config CONFIG_FILE         Configuration file path"
            echo "  --env ENVIRONMENT            Environment (dev/staging/prod)"
            echo "  --help, -h                   Show this help message"
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

# Set defaults if not provided
SPARK_MASTER="${SPARK_MASTER:-$DEFAULT_MASTER}"
DRIVER_MEMORY="${DRIVER_MEMORY:-$DEFAULT_DRIVER_MEMORY}"
EXECUTOR_MEMORY="${EXECUTOR_MEMORY:-$DEFAULT_EXECUTOR_MEMORY}"
EXECUTOR_CORES="${EXECUTOR_CORES:-$DEFAULT_EXECUTOR_CORES}"
NUM_EXECUTORS="${NUM_EXECUTORS:-$DEFAULT_NUM_EXECUTORS}"
CONFIG_FILE="${CONFIG_FILE:-job-config.yaml}"
ENVIRONMENT="${ENVIRONMENT:-dev}"

# Find JAR file
JAR_FILE=$(find "${JOB_DIR}" -name "etl-jobs-*.jar" | head -1)
if [[ -z "$JAR_FILE" ]]; then
    echo "Error: ETL jobs JAR file not found in ${JOB_DIR}"
    exit 1
fi

# Validate required files
if [[ ! -f "${JOB_DIR}/${CONFIG_FILE}" ]]; then
    echo "Error: Configuration file ${CONFIG_FILE} not found in ${JOB_DIR}"
    exit 1
fi

if [[ ! -f "${JOB_DIR}/application.properties" ]]; then
    echo "Error: application.properties file not found in ${JOB_DIR}"
    exit 1
fi

if [[ ! -f "${JOB_DIR}/logback.xml" ]]; then
    echo "Error: logback.xml file not found in ${JOB_DIR}"
    exit 1
fi

echo "=== Spark ETL Framework Job Submission ==="
echo "JAR File: $JAR_FILE"
echo "Config File: $CONFIG_FILE"
echo "Environment: $ENVIRONMENT"
echo "Spark Master: $SPARK_MASTER"
echo "Driver Memory: $DRIVER_MEMORY"
echo "Executor Memory: $EXECUTOR_MEMORY"
echo "Executor Cores: $EXECUTOR_CORES"
echo "Number of Executors: $NUM_EXECUTORS"

# Determine deployment mode based on master
if [[ "$SPARK_MASTER" == local* ]]; then
    DEPLOY_MODE="client"
else
    DEPLOY_MODE="cluster"
fi

# Build spark-submit command
SPARK_SUBMIT_CMD=(
    spark-submit
    --master "$SPARK_MASTER"
    --deploy-mode "$DEPLOY_MODE"
    --driver-memory "$DRIVER_MEMORY"
    --executor-memory "$EXECUTOR_MEMORY"
    --executor-cores "$EXECUTOR_CORES"
    --num-executors "$NUM_EXECUTORS"
    --conf "spark.sql.adaptive.enabled=true"
    --conf "spark.sql.adaptive.coalescePartitions.enabled=true"
    --conf "spark.serializer=org.apache.spark.serializer.KryoSerializer"
    --files "${JOB_DIR}/application.properties,${JOB_DIR}/${CONFIG_FILE},${JOB_DIR}/logback.xml"
    --driver-java-options "-Dlogback.configurationFile=logback.xml"
    --conf "spark.executor.extraJavaOptions=-Dlogback.configurationFile=logback.xml"
    --class "com.etl.jobs.sample.SampleETLJob"
    "$JAR_FILE"
    --config "$CONFIG_FILE"
)

# Add job name if provided
if [[ -n "$JOB_NAME" ]]; then
    SPARK_SUBMIT_CMD+=(--job-name "$JOB_NAME")
fi

echo "=== Executing Spark Submit Command ==="
echo "${SPARK_SUBMIT_CMD[@]}"
echo "=========================="

# Execute the command
"${SPARK_SUBMIT_CMD[@]}"

exit_code=$?

if [[ $exit_code -eq 0 ]]; then
    echo "=== Job completed successfully ==="
else
    echo "=== Job failed with exit code $exit_code ==="
    exit $exit_code
fi
