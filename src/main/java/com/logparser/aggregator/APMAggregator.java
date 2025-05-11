package com.logparser.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.logparser.model.APMLogEntry;
import com.logparser.model.LogEntry;

// Concrete aggregator implementation for APM logs
public class APMAggregator implements LogAggregator {
    private Map<String, List<Double>> metricValues = new HashMap<>();
    
    @Override
    public void addLogEntry(LogEntry entry) {
        if (entry instanceof APMLogEntry) {
            APMLogEntry apmEntry = (APMLogEntry) entry;
            String metric = apmEntry.getMetric();
            double value = apmEntry.getValue();
            
            metricValues.computeIfAbsent(metric, k -> new ArrayList<>()).add(value);
        }
    }
    
    @Override
    public Map<String, Object> getAggregatedData() {
        Map<String, Object> result = new HashMap<>();
        
        for (Map.Entry<String, List<Double>> entry : metricValues.entrySet()) {
            String metric = entry.getKey();
            List<Double> values = entry.getValue();
            
            if (!values.isEmpty()) {
                Map<String, Object> metricStats = new HashMap<>();
                
                // Calculate metrics
                double min = Collections.min(values);
                double max = Collections.max(values);
                double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                double median = calculateMedian(values);
                
                metricStats.put("minimum", min);
                metricStats.put("median", median);
                metricStats.put("average", avg);
                metricStats.put("max", max);
                
                result.put(metric, metricStats);
            }
        }
        
        return result;
    }
    
    private double calculateMedian(List<Double> values) {
        List<Double> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);
        
        int size = sortedValues.size();
        if (size % 2 == 0) {
            return (sortedValues.get(size / 2 - 1) + sortedValues.get(size / 2)) / 2.0;
        } else {
            return sortedValues.get(size / 2);
        }
    }
}