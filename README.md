[![Java CI with Maven](https://github.com/amitnema/spark-etl-framework/actions/workflows/maven.yml/badge.svg)](https://github.com/amitnema/spark-etl-framework/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/amitnema/spark-etl-framework/branch/main/graph/badge.svg?token=YOUR_CODECOV_TOKEN)](https://codecov.io/gh/amitnema/spark-etl-framework)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Spark ETL Framework

A generic, multimodule framework for building scalable ETL (Extract, Transform, Load) processes using Apache Spark and Java. This framework is designed to be cloud-agnostic, maintainable, testable, and production-ready.

## Architecture Overview

The framework follows enterprise design patterns and consists of the following modules:

- **etl-core**: Core framework components including configuration, validation, transformation interfaces, and I/O operations
- **etl-jobs**: Concrete job implementations and custom transformers

## Key Features

- ✅ **Cloud Agnostic**: Supports AWS S3, Google Cloud Storage, Azure Blob Storage
- ✅ **Modular Design**: Separation of concerns with pluggable components  
- ✅ **Configuration-Driven**: YAML-based job configuration with parameter substitution
- ✅ **Data Validation**: Built-in validation rules (NOT_NULL, UNIQUE, RANGE, REGEX)
- ✅ **Multiple Data Sources**: File systems, databases (JDBC), streaming sources
- ✅ **Format Support**: Parquet, JSON, CSV, Avro, ORC
- ✅ **Transformation Framework**: Abstract transformer pattern with custom implementations
- ✅ **Error Handling**: Comprehensive exception handling and logging
- ✅ **Production Ready**: Docker containerization, monitoring, and deployment scripts
- ✅ **Testing Support**: Unit and integration testing capabilities

## Project Structure

```
spark-etl-framework/
├── etl-core/                    # Core framework module
│   └── src/main/java/com/etl/core/
│       ├── config/              # Configuration management
│       ├── model/               # Data models and POJOs
│       ├── io/                  # Data readers and writers
│       ├── validation/          # Data validation components
│       ├── transformation/      # Transformation interfaces
│       ├── factory/             # Factory pattern implementations
│       ├── exception/           # Custom exceptions
│       └── utils/               # Utility classes
├── etl-jobs/                    # Job implementations
│   └── src/main/java/com/etl/jobs/
│       └── sample/              # Sample job implementation
├── dist/                        # Distribution packages
│   └── sample-job/              # Ready-to-deploy job package
├── docker/                      # Docker configuration
├── scripts/                     # Build and deployment scripts
└── README.md                    # This file
```

## Design Patterns Used

1. **Factory Pattern**: For creating readers, writers, and transformers
2. **Abstract Template Pattern**: For transformation pipeline
3. **Strategy Pattern**: For different I/O implementations
4. **Builder Pattern**: For configuration objects
5. **Singleton Pattern**: For configuration management
6. **Command Pattern**: For job execution

## Quick Start

### Prerequisites

- Java 11 or higher
- Apache Maven 3.6+
- Apache Spark 3.4.0
- Docker (optional)

### Building the Framework

```bash
# Clone the repository
git clone <repository-url>
cd spark-etl-framework

# Build the project
mvn clean package

# Or use the build script
./scripts/build.sh
```

### Running Sample Job

```bash
# Navigate to the distribution directory
cd dist/sample-job/

# Execute the job
./spark-submit.sh --master local[*] --config job-config.yaml
```

### Custom Job Configuration

Create a `job-config.yaml` file:

```yaml
jobName: "my-etl-job"
jobDescription: "Custom ETL job"
jobVersion: "1.0.0"

inputs:
  - name: "source_data"
    type: "file"
    format: "parquet"
    path: "s3a://my-bucket/input/data.parquet"

transformation:
  className: "org.apn.etl.jobs.custom.MyTransformer"
  parameters:
    filterDate: "2023-01-01"
    outputFormat: "processed"

outputs:
  - name: "processed_data"
    type: "file"
    format: "parquet"
    path: "s3a://my-bucket/output/processed.parquet"
    mode: "overwrite"

validation:
  enabled: true
  rules:
    - name: "check_not_null_id"
      type: "NOT_NULL"
      column: "id"
```

## Development Guide

### Creating Custom Transformers

Extend the `AbstractDataTransformer` class:

```java
public class MyCustomTransformer extends AbstractDataTransformer {
    @Override
    protected Dataset<Row> doTransform(Dataset<Row> input, Map<String, Object> parameters) {
        // Your transformation logic here
        return input.filter(functions.col("status").equalTo("active"));
    }
}
```

### Adding New Data Sources

Implement the `DataReader` interface:

```java
public class MyCustomReader implements DataReader {
    @Override
    public Dataset<Row> read(InputConfig config) {
        // Your data reading logic here
        return dataset;
    }
}
```

### Configuration Management

The framework uses a hierarchical configuration approach:

1. `application.properties` - Application-wide settings
2. `job-config.yaml` - Job-specific configuration
3. Command-line parameters - Runtime overrides

## Cloud Deployment

### AWS Configuration

```properties
cloud.provider=aws
aws.access.key=YOUR_ACCESS_KEY
aws.secret.key=YOUR_SECRET_KEY
aws.region=us-east-1
```

### GCP Configuration

```properties
cloud.provider=gcp
gcp.project.id=your-project-id
gcp.service.account.key=/path/to/service-account.json
```

### Azure Configuration

```properties
cloud.provider=azure
azure.storage.account=your-storage-account
azure.storage.access.key=your-access-key
```

## Docker Deployment

```bash
# Build Docker image
cd docker/
docker-compose build

# Run with Docker Compose
docker-compose up -d

# Execute ETL job in container
docker-compose exec etl-framework ./sample-job/spark-submit.sh
```

## Monitoring and Logging

The framework includes comprehensive logging using Logback:

- **Console Output**: Real-time job progress
- **File Logging**: Detailed logs with rotation
- **Structured Logging**: JSON format for log aggregation
- **Metrics**: Job execution metrics and validation results

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify -P integration-tests
```

## Performance Tuning

### Spark Configuration

Key configuration parameters in `application.properties`:

```properties
spark.sql.adaptive.enabled=true
spark.sql.adaptive.coalescePartitions.enabled=true
spark.sql.adaptive.skewJoin.enabled=true
spark.serializer=org.apache.spark.serializer.KryoSerializer
```

### Memory Settings

Adjust memory settings in spark-submit script:

```bash
--driver-memory 4g
--executor-memory 8g
--executor-cores 4
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Implement your changes
4. Add comprehensive tests
5. Update documentation
6. Submit a pull request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

For support and questions:
- Create an issue in the repository
- Check the [Wiki](wiki-url) for additional documentation
- Contact the development team

## Roadmap

- [ ] Kafka streaming support
- [ ] Delta Lake integration
- [ ] Advanced data profiling
- [ ] ML pipeline integration
- [ ] REST API for job management
- [ ] Web UI for monitoring

---

Built with ❤️ using Apache Spark and Java
