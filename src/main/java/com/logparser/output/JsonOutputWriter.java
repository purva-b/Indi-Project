package com.logparser.output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// JSON output writer implementation
public class JsonOutputWriter implements OutputWriter {
    private final Gson gson;
    
    public JsonOutputWriter() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    @Override
    public void write(String filename, Map<String, Object> data) {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
            System.out.println("Successfully wrote output to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
        }
    }
}