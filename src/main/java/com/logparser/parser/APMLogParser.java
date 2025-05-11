package com.logparser.parser;

import java.util.HashMap;
import java.util.Map;

import com.logparser.model.APMLogEntry;
import com.logparser.model.LogEntry;

// Concrete parser implementation for APM logs
public class APMLogParser implements LogEntryParser {
    @Override
    public LogEntry parse(String logLine) {
        Map<String, String> fields = parseLogFields(logLine);
        
        String timestamp = fields.get("timestamp");
        String host = fields.get("host");
        String metric = fields.get("metric");
        double value = Double.parseDouble(fields.get("value"));
        
        APMLogEntry entry = new APMLogEntry(timestamp, host, metric, value);
        
        // Add any additional fields
        for (Map.Entry<String, String> field : fields.entrySet()) {
            String key = field.getKey();
            if (!key.equals("timestamp") && !key.equals("host") && 
                !key.equals("metric") && !key.equals("value")) {
                entry.addField(key, field.getValue());
            }
        }
        
        return entry;
    }
    
    private Map<String, String> parseLogFields(String logLine) {
        Map<String, String> fields = new HashMap<>();
        String[] parts = logLine.split("\\s+");
        
        for (String part : parts) {
            int equalsPos = part.indexOf('=');
            if (equalsPos > 0) {
                String key = part.substring(0, equalsPos);
                String value = part.substring(equalsPos + 1).replaceAll("^\"|\"$", "");
                fields.put(key, value);
            }
        }
        
        return fields;
    }
}