#!/bin/bash

# ETL Framework Build Script

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DIST_DIR="${PROJECT_DIR}/dist"

echo "=== Building ETL Framework ==="
echo "Project directory: $PROJECT_DIR"

# Clean and build the project
echo "Cleaning and building Maven project..."
cd "$PROJECT_DIR"
mvn clean package -DskipTests

echo "=== Creating distribution packages ==="

# Create dist directory if it doesn't exist
mkdir -p "$DIST_DIR"

# Find the built JAR
JAR_FILE=$(find "${PROJECT_DIR}/etl-jobs/target" -name "etl-jobs-*.jar" | grep -v original | head -1)
if [[ -z "$JAR_FILE" ]]; then
    echo "Error: Built JAR file not found"
    exit 1
fi

echo "Found JAR file: $JAR_FILE"

# Copy JAR to sample-job directory
cp "$JAR_FILE" "${DIST_DIR}/sample-job/"

# Make scripts executable
chmod +x "${DIST_DIR}/sample-job/spark-submit.sh"

echo "=== Creating ZIP distribution ==="

# Create ZIP file for sample job
cd "$DIST_DIR"
zip -r "sample-job-$(date +%Y%m%d_%H%M%S).zip" sample-job/

echo "=== Build completed successfully ==="
echo "Distribution files created in: $DIST_DIR"
echo "Ready to deploy!"
