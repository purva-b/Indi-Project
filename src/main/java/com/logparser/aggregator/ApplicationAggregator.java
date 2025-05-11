package com.logparser.aggregator;

import java.util.HashMap;
import java.util.Map;

import com.logparser.model.ApplicationLogEntry;
import com.logparser.model.LogEntry;

// Concrete aggregator implementation for Application logs
public class ApplicationAggregator implements LogAggregator {
    private Map<String, Integer> severityCounts = new HashMap<>();
    
    @Override
    public void addLogEntry(LogEntry entry) {
        if (entry instanceof ApplicationLogEntry) {
            ApplicationLogEntry appEntry = (ApplicationLogEntry) entry;
            String level = appEntry.getLevel();
            
            severityCounts.put(level, severityCounts.getOrDefault(level, 0) + 1);
        }
    }
    
    @Override
    public Map<String, Object> getAggregatedData() {
        // For Application logs, the aggregated data is simply the severity counts
        return new HashMap<>(severityCounts);
    }
}