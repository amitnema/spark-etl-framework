package org.apn.etl.core.model;

import java.util.List;

/**
 * Partition configuration model
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

    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }

    public int getNumPartitions() { return numPartitions; }
    public void setNumPartitions(int numPartitions) { this.numPartitions = numPartitions; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
}