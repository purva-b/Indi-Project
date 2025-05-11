package com.logparser.parser;

import java.util.HashMap;
import java.util.Map;

import com.logparser.model.ApplicationLogEntry;
import com.logparser.model.LogEntry;

// Concrete parser implementation for Application logs
public class ApplicationLogParser implements LogEntryParser {
    @Override
    public LogEntry parse(String logLine) {
        Map<String, String> fields = parseLogFields(logLine);
        
        String timestamp = fields.get("timestamp");
        String host = fields.get("host");
        String level = fields.get("level");
        String message = fields.get("message");
        
        ApplicationLogEntry entry = new ApplicationLogEntry(timestamp, host, level, message);
        
        // Add any additional fields
        for (Map.Entry<String, String> field : fields.entrySet()) {
            String key = field.getKey();
            if (!key.equals("timestamp") && !key.equals("host") && 
                !key.equals("level") && !key.equals("message")) {
                entry.addField(key, field.getValue());
            }
        }
        
        return entry;
    }
    
    private Map<String, String> parseLogFields(String logLine) {
        Map<String, String> fields = new HashMap<>();
        StringBuilder currentPart = new StringBuilder();
        boolean inQuotes = false;
        String currentKey = null;
        
        for (int i = 0; i < logLine.length(); i++) {
            char c = logLine.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
                currentPart.append(c);
            } else if (c == '=' && !inQuotes) {
                currentKey = currentPart.toString();
                currentPart = new StringBuilder();
            } else if (c == ' ' && !inQuotes) {
                if (currentKey != null && currentPart.length() > 0) {
                    String value = currentPart.toString();
                    // Remove quotes if present
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    fields.put(currentKey, value);
                    currentKey = null;
                }
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }
        
        // Add the last field
        if (currentKey != null && currentPart.length() > 0) {
            String value = currentPart.toString();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            fields.put(currentKey, value);
        }
        
        return fields;
    }
}