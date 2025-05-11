package com.logparser.aggregator;

import java.util.Map;

import com.logparser.model.LogEntry;

// Observer Pattern: Aggregators observe and collect log data
public interface LogAggregator {
    void addLogEntry(LogEntry entry);
    Map<String, Object> getAggregatedData();
}