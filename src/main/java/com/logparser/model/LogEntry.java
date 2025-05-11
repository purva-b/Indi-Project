package com.logparser.model;

import com.logparser.LogType;
// Base class for all log entries
public abstract class LogEntry {
    protected String timestamp;
    protected String host;
    
    public abstract LogType getType();
}