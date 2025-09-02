package org.apn.etl.core.model;

import java.util.List;

/**
 * Partition configuration model for ETL jobs.
 * <p>
 * Defines partitioning strategy and columns for output data.
 * </p>
 *
 * @author Amit Prakash Nema
 */
public class PartitionConfig {
    private List<String> columns;
    private int numPartitions;
    private String strategy; // HASH, RANGE, COLUMN

    public PartitionConfig() {}

    public PartitionConfig(List<String> columns, String strategy) {
        this.columns = columns;
        this.strategy = strategy;
    }

    /**
     * Gets the partition columns.
     * @return list of partition columns
     */
    public List<String> getColumns() { return columns; }
    /**
     * Sets the partition columns.
     * @param columns list of partition columns
     */
    public void setColumns(List<String> columns) { this.columns = columns; }

    /**
     * Gets the number of partitions.
     * @return number of partitions
     */
    public int getNumPartitions() { return numPartitions; }
    /**
     * Sets the number of partitions.
     * @param numPartitions number of partitions
     */
    public void setNumPartitions(int numPartitions) { this.numPartitions = numPartitions; }

    /**
     * Gets the partitioning strategy (HASH, RANGE, COLUMN).
     * @return strategy
     */
    public String getStrategy() { return strategy; }
    /**
     * Sets the partitioning strategy.
     * @param strategy strategy
     */
    public void setStrategy(String strategy) { this.strategy = strategy; }
}