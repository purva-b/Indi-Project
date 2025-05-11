package com.logparser.parser;

import com.logparser.model.LogEntry;
import com.logparser.parser.APMLogParser;
import com.logparser.parser.ApplicationLogParser;
import com.logparser.parser.RequestLogParser;
// Factory Method Pattern: Creating appropriate parsers
public class LogParserFactory {
    public LogEntryParser createParser(String logLine) {
        if (logLine.contains("metric=") && logLine.contains("value=")) {
            return new APMLogParser();
        } else if (logLine.contains("level=")) {
            return new ApplicationLogParser();
        } else if (logLine.contains("request_method=") && logLine.contains("request_url=")) {
            return new RequestLogParser();
        }
        return null; // No matching parser for this log line
    }
}