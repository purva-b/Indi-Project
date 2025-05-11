# Log Parser Application

This application processes log files containing different types of log entries (APM logs, Application logs, and Request logs), classifies them, and generates aggregated statistics in JSON format.

## Overview

The Log Parser application is a command-line tool that analyzes log files and produces useful statistical summaries for different log types. It demonstrates the practical application of several design patterns to create a flexible, extensible, and maintainable system.

## Features

- Parses log entries from text files
- Classifies logs into three categories:
  - APM (Application Performance Metrics) logs
  - Application logs (ERROR, WARNING, INFO, etc.)
  - Request logs (API calls with response times and status codes)
- Performs specialized aggregations for each log type:
  - APM: min, median, average, max values for each metric
  - Application: count by severity level
  - Request: response time statistics and status code counts by API route
- Outputs results to separate JSON files
- Handles corrupted or invalid log entries gracefully
- Designed for extensibility to support additional log types and formats

## Design Patterns

The application implements four key design patterns:

1. **Strategy Pattern**:
   - Interface: `LogEntryParser`
   - Implementations: `APMLogParser`, `ApplicationLogParser`, `RequestLogParser` 
   - Purpose: Enables different parsing strategies for different log types

2. **Factory Method Pattern**:
   - Class: `LogParserFactory`
   - Purpose: Creates appropriate parser instances based on log content

3. **Observer Pattern**:
   - Interface: `LogAggregator`
   - Implementations: `APMAggregator`, `ApplicationAggregator`, `RequestAggregator`
   - Purpose: Collects and processes log data independently for each type

4. **Template Method Pattern**:
   - Class: `LogProcessor`
   - Purpose: Defines the overall workflow for log processing

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Building the Application

```bash
mvn clean package
```

This will compile the code, run tests, and create an executable JAR file in the `target` directory.

### Running the Application

```bash
java -jar target/log-parser-1.0-jar-with-dependencies.jar --file sample_logs.txt
```

### Sample Log Format

The application supports log entries in the following formats:

#### APM Logs
```
timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72
```

#### Application Logs
```
timestamp=2024-02-24T16:22:20Z level=INFO message="Scheduled maintenance starting" host=webserver1
```

#### Request Logs
```
timestamp=2024-02-24T16:22:25Z request_method=POST request_url="/api/update" response_status=202 response_time_ms=200 host=webserver1
```

### Output Files

The application generates three JSON files:

1. `apm.json` - Contains aggregated APM metrics
2. `application.json` - Contains log counts by severity level
3. `request.json` - Contains response time statistics and status code counts by API route

## Testing

Run the tests with:

```bash
mvn test
```

The tests verify the parsing logic, aggregation functionality, and end-to-end log processing.

## License

This project is available under the MIT License.
