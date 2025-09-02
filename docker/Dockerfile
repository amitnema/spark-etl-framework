# Stage 1: Build (if you have a build step, e.g., Maven)
FROM maven:3.9.5-eclipse-temurin-11 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
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

WORKDIR /opt/etl-framework

# Copy only built artifacts and configs from build stage
COPY --from=build /build/etl-jobs/target/etl-jobs-*.jar ./
COPY --from=build /build/etl-jobs/src/main/resources/jobs/sample-job/ ./sample-job/

# Make scripts executable if present
RUN [ -f ./sample-job/spark-submit.sh ] && chmod +x ./sample-job/spark-submit.sh || true

ENV JAVA_HOME=/usr/local/openjdk-11
ENV ETL_HOME=/opt/etl-framework

# Default entrypoint: run Spark job, configurable via env
ENTRYPOINT ["/opt/spark/bin/spark-submit"]
CMD ["--class", "${MAIN_CLASS:-org.apn.etl.jobs.Main}", "etl-jobs-1.0.0.jar"]
