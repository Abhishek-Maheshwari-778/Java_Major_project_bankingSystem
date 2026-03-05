package com.banking.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "ModernBankingSystem/data/banking_system.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) { log("INFO", message); }
    public static void warn(String message) { log("WARN", message); }
    public static void error(String message, Exception e) {
        log("ERROR", message + (e != null ? ": " + e.getMessage() : ""));
        if (e != null) e.printStackTrace();
    }

    private static void log(String level, String message) {
        String logEntry = String.format("[%s] [%s] %s", LocalDateTime.now().format(formatter), level, message);
        System.out.println(logEntry);
        try (FileWriter fw = new FileWriter(LOG_FILE, true); PrintWriter pw = new PrintWriter(fw)) {
            pw.println(logEntry);
        } catch (IOException e) { System.err.println("Log error: " + e.getMessage()); }
    }
}
