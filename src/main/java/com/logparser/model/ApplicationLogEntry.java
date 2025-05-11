package com.logparser.model;

import java.util.HashMap;
import java.util.Map;
import com.logparser.LogType;

/**
 * Concrete log entry implementation for Application logs
 */
public class ApplicationLogEntry extends LogEntry {
    private String level;
    private String message;
    private Map<String, String> additionalFields = new HashMap<>();
    
    public ApplicationLogEntry(String timestamp, String host, String level, String message) {
        this.timestamp = timestamp;
        this.host = host;
        this.level = level;
        this.message = message;
    }
    
    public void addField(String key, String value) {
        additionalFields.put(key, value);
    }
    
    public String getLevel() {
        return level;
    }
    
    @Override
    public LogType getType() {
        return LogType.APPLICATION;
    }
}