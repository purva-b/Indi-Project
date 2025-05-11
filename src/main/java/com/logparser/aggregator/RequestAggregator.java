package com.logparser.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.logparser.model.LogEntry;
import com.logparser.model.RequestLogEntry;

// Concrete aggregator implementation for Request logs
public class RequestAggregator implements LogAggregator {
    private Map<String, List<Integer>> responseTimesByRoute = new HashMap<>();
    private Map<String, Map<String, Integer>> statusCodesByRoute = new HashMap<>();
    
    @Override
    public void addLogEntry(LogEntry entry) {
        if (entry instanceof RequestLogEntry) {
            RequestLogEntry requestEntry = (RequestLogEntry) entry;
            String route = requestEntry.getRequestUrl();
            int responseTime = requestEntry.getResponseTimeMs();
            int statusCode = requestEntry.getResponseStatus();
            
            // Add response time for this route
            responseTimesByRoute.computeIfAbsent(route, k -> new ArrayList<>()).add(responseTime);
            
            // Categorize the status code
            String category = getStatusCodeCategory(statusCode);
            
            // Update status code counts for this route
            statusCodesByRoute.computeIfAbsent(route, k -> new HashMap<>())
                             .put(category, statusCodesByRoute.get(route)
                                                           .getOrDefault(category, 0) + 1);
        }
    }
    
    @Override
    public Map<String, Object> getAggregatedData() {
        Map<String, Object> result = new HashMap<>();
        
        for (String route : responseTimesByRoute.keySet()) {
            Map<String, Object> routeStats = new HashMap<>();
            
            // Calculate response time statistics
            List<Integer> responseTimes = responseTimesByRoute.get(route);
            Map<String, Object> responseTimeStats = calculateResponseTimeStats(responseTimes);
            routeStats.put("response_times", responseTimeStats);
            
            // Add status code statistics
            Map<String, Integer> statusCodes = statusCodesByRoute.getOrDefault(route, new HashMap<>());
            Map<String, Object> statusCodeStats = new HashMap<>(statusCodes);
            
            // Ensure all categories are present
            ensureStatusCodeCategories(statusCodeStats);
            
            routeStats.put("status_codes", statusCodeStats);
            
            result.put(route, routeStats);
        }
        
        return result;
    }
    
    private String getStatusCodeCategory(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) {
            return "2XX";
        } else if (statusCode >= 400 && statusCode < 500) {
            return "4XX";
        } else if (statusCode >= 500 && statusCode < 600) {
            return "5XX";
        } else {
            return "OTHER";
        }
    }
    
    private Map<String, Object> calculateResponseTimeStats(List<Integer> responseTimes) {
        Map<String, Object> stats = new HashMap<>();
        
        if (responseTimes.isEmpty()) {
            return stats;
        }
        
        // Sort response times for percentile calculations
        List<Integer> sortedTimes = new ArrayList<>(responseTimes);
        Collections.sort(sortedTimes);
        
        int min = sortedTimes.get(0);
        int max = sortedTimes.get(sortedTimes.size() - 1);
        
        // Calculate percentiles
        int p50 = calculatePercentile(sortedTimes, 50);
        int p90 = calculatePercentile(sortedTimes, 90);
        int p95 = calculatePercentile(sortedTimes, 95);
        int p99 = calculatePercentile(sortedTimes, 99);
        
        stats.put("min", min);
        stats.put("50_percentile", p50);
        stats.put("90_percentile", p90);
        stats.put("95_percentile", p95);
        stats.put("99_percentile", p99);
        stats.put("max", max);
        
        return stats;
    }
    
    private int calculatePercentile(List<Integer> sortedValues, int percentile) {
        int index = (int) Math.ceil(percentile / 100.0 * sortedValues.size()) - 1;
        return sortedValues.get(Math.max(0, Math.min(sortedValues.size() - 1, index)));
    }
    
    private void ensureStatusCodeCategories(Map<String, Object> statusCodeStats) {
        // Ensure all status code categories are present
        if (!statusCodeStats.containsKey("2XX")) {
            statusCodeStats.put("2XX", 0);
        }
        if (!statusCodeStats.containsKey("4XX")) {
            statusCodeStats.put("4XX", 0);
        }
        if (!statusCodeStats.containsKey("5XX")) {
            statusCodeStats.put("5XX", 0);
        }
    }
}