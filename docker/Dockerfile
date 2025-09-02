FROM openjdk:11-jre-slim

# Install required packages
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    procps \
    && rm -rf /var/lib/apt/lists/*

# Install Spark
ENV SPARK_VERSION=3.4.0
ENV HADOOP_VERSION=3
ENV SPARK_HOME=/opt/spark
ENV PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

RUN wget -q "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz" \
    && tar xzf "spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz" -C /opt/ \
    && ln -s "/opt/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}" "${SPARK_HOME}" \
    && rm "spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz"

# Create ETL framework directory
WORKDIR /opt/etl-framework

# Copy framework JARs and configurations
COPY etl-jobs/target/etl-jobs-*.jar ./
COPY dist/sample-job/ ./sample-job/

# Make scripts executable
RUN chmod +x ./sample-job/spark-submit.sh

# Set environment variables
ENV JAVA_HOME=/usr/local/openjdk-11
ENV ETL_HOME=/opt/etl-framework

# Default command
CMD ["bash"]
