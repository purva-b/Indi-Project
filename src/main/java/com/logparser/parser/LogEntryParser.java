package com.logparser.parser;

import com.logparser.model.LogEntry;
// Strategy Pattern: Different parsing strategies
public interface LogEntryParser {
    LogEntry parse(String logLine);
}