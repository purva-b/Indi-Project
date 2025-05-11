package com.logparser.parser;

import java.util.HashMap;
import java.util.Map;

import com.logparser.model.LogEntry;
import com.logparser.model.RequestLogEntry;

// Concrete parser implementation for Request logs
public class RequestLogParser implements LogEntryParser {
    @Override
    public LogEntry parse(String logLine) {
        Map<String, String> fields = parseLogFields(logLine);
        
        String timestamp = fields.get("timestamp");
        String host = fields.get("host");
        String requestMethod = fields.get("request_method");
        String requestUrl = fields.get("request_url");
        int responseStatus = Integer.parseInt(fields.get("response_status"));
        int responseTimeMs = Integer.parseInt(fields.get("response_time_ms"));
        
        RequestLogEntry entry = new RequestLogEntry(timestamp, host, requestMethod, 
                                                   requestUrl, responseStatus, responseTimeMs);
        
        // Add any additional fields
        for (Map.Entry<String, String> field : fields.entrySet()) {
            String key = field.getKey();
            if (!key.equals("timestamp") && !key.equals("host") && 
                !key.equals("request_method") && !key.equals("request_url") &&
                !key.equals("response_status") && !key.equals("response_time_ms")) {
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
