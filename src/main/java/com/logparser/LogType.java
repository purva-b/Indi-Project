package com.logparser;

/**
 * Enum for log types with their associated output file names
 */
public enum LogType {
    APM("apm.json"),
    APPLICATION("application.json"),
    REQUEST("request.json");
    
    private final String outputFileName;
    
    LogType(String outputFileName) {
        this.outputFileName = outputFileName;
    }
    
    public String getOutputFileName() {
        return outputFileName;
    }
}