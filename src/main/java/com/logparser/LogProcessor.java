package com.logparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.logparser.aggregator.APMAggregator;
import com.logparser.aggregator.ApplicationAggregator;
import com.logparser.aggregator.LogAggregator;
import com.logparser.aggregator.RequestAggregator;
import com.logparser.model.LogEntry;
import com.logparser.output.JsonOutputWriter;
import com.logparser.output.OutputWriter;
import com.logparser.parser.LogEntryParser;
import com.logparser.parser.LogParserFactory;

// The Template Method pattern implementation
public class LogProcessor {
    private final String inputFile;
    private final LogParserFactory parserFactory;
    private final Map<LogType, LogAggregator> aggregators;
    private final OutputWriter outputWriter;

    public LogProcessor(String inputFile) {
        this.inputFile = inputFile;
        this.parserFactory = new LogParserFactory();
        this.aggregators = new HashMap<>();
        
        // Initialize aggregators for each log type
        aggregators.put(LogType.APM, new APMAggregator());
        aggregators.put(LogType.APPLICATION, new ApplicationAggregator());
        aggregators.put(LogType.REQUEST, new RequestAggregator());
        
        this.outputWriter = new JsonOutputWriter();
    }

    // Template method defining the workflow
    public void process() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLogLine(line);
            }
            
            writeOutputs();
        } catch (IOException e) {
            System.err.println("Error processing log file: " + e.getMessage());
        }
    }

    private void processLogLine(String line) {
        try {
            // Determine the log type and get the appropriate parser
            LogEntryParser parser = parserFactory.createParser(line);
            if (parser != null) {
                // Parse the line into a log entry
                LogEntry entry = parser.parse(line);
                
                // Add the entry to the corresponding aggregator
                if (entry != null) {
                    aggregators.get(entry.getType()).addLogEntry(entry);
                }
            }
        } catch (Exception e) {
            // Skip corrupted or incompatible log lines
            System.err.println("Skipping invalid log line: " + line);
        }
    }

    private void writeOutputs() {
        // Write each aggregation to its corresponding output file
        for (Map.Entry<LogType, LogAggregator> entry : aggregators.entrySet()) {
            LogType type = entry.getKey();
            LogAggregator aggregator = entry.getValue();
            
            String filename = type.getOutputFileName();
            outputWriter.write(filename, aggregator.getAggregatedData());
        }
    }
}