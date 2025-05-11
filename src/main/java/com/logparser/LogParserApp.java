package com.logparser;


public class LogParserApp {
    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equals("--file")) {
            System.out.println("Usage: java LogParserApp --file <filename.txt>");
            return;
        }

        String inputFile = args[1];
        LogProcessor processor = new LogProcessor(inputFile);
        processor.process();
    }
}