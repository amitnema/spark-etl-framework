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
package org.apn.etl.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Partition configuration model for ETL jobs.
 *
 * <p>Defines partitioning strategy and columns for output data.
 *
 * @author Amit Prakash Nema
 */
public final class PartitionConfig {
  private List<String> columns;
  private int numPartitions;
  private String strategy; // HASH, RANGE, COLUMN

  public PartitionConfig() {}

  public PartitionConfig(final List<String> columns, final String strategy) {
    this.columns = new ArrayList<>(columns);
    this.strategy = strategy;
  }

  public PartitionConfig(final PartitionConfig other) {
    this.columns = new ArrayList<>(other.columns);
    this.numPartitions = other.numPartitions;
    this.strategy = other.strategy;
  }

  /**
   * Gets the partition columns.
   *
   * @return list of partition columns
   */
  public List<String> getColumns() {
    return new ArrayList<>(columns);
  }

  /**
   * Sets the partition columns.
   *
   * @param columns list of partition columns
   */
  public void setColumns(final List<String> columns) {
    this.columns = new ArrayList<>(columns);
  }

  /**
   * Gets the number of partitions.
   *
   * @return number of partitions
   */
  public int getNumPartitions() {
    return numPartitions;
  }

  /**
   * Sets the number of partitions.
   *
   * @param numPartitions number of partitions
   */
  public void setNumPartitions(final int numPartitions) {
    this.numPartitions = numPartitions;
  }

  /**
   * Gets the partitioning strategy (HASH, RANGE, COLUMN).
   *
   * @return strategy
   */
  public String getStrategy() {
    return strategy;
  }

  /**
   * Sets the partitioning strategy.
   *
   * @param strategy strategy
   */
  public void setStrategy(final String strategy) {
    this.strategy = strategy;
  }
}
