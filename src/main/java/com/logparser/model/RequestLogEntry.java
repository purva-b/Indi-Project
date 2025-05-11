package com.logparser.model;

import java.util.HashMap;
import java.util.Map;

import com.logparser.LogType;

// Concrete log entry implementation for Request logs
public class RequestLogEntry extends LogEntry {
    private String requestMethod;
    private String requestUrl;
    private int responseStatus;
    private int responseTimeMs;
    private Map<String, String> additionalFields = new HashMap<>();
    
    public RequestLogEntry(String timestamp, String host, String requestMethod, 
                          String requestUrl, int responseStatus, int responseTimeMs) {
        this.timestamp = timestamp;
        this.host = host;
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseTimeMs = responseTimeMs;
    }
    
    public void addField(String key, String value) {
        additionalFields.put(key, value);
    }
    
    public String getRequestUrl() {
        return requestUrl;
    }
    
    public int getResponseStatus() {
        return responseStatus;
    }
    
    public int getResponseTimeMs() {
        return responseTimeMs;
    }
    
    @Override
    public LogType getType() {
        return LogType.REQUEST;
    }
}
