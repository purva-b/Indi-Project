package com.logparser.output;
import java.util.Map;

// Output writer interface
public interface OutputWriter {
    void write(String filename, Map<String, Object> data);
}