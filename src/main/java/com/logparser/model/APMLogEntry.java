package com.logparser.model;

import java.util.HashMap;
import java.util.Map;

import com.logparser.LogType;

// Concrete log entry implementation for APM logs
public class APMLogEntry extends LogEntry {
    private String metric;
    private double value;
    private Map<String, String> additionalFields = new HashMap<>();
    
    public APMLogEntry(String timestamp, String host, String metric, double value) {
        this.timestamp = timestamp;
        this.host = host;
        this.metric = metric;
        this.value = value;
    }
    
    public void addField(String key, String value) {
        additionalFields.put(key, value);
    }
    
    public String getMetric() {
        return metric;
    }
    
    public double getValue() {
        return value;
    }
    
    @Override
    public LogType getType() {
        return LogType.APM;
    }
}