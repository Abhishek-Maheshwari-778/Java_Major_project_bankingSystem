package com.banking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_NAME = "modern_banking_db";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static Connection getInitialConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true", USER, PASS);
    }
}
