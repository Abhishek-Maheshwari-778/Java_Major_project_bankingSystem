package com.banking.db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {
    public static void setup() {
        try (Connection conn = DatabaseConnection.getInitialConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS modern_banking_db;");
            stmt.executeUpdate("USE modern_banking_db;");

            // Users Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(50) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL, " +
                    "name VARCHAR(100), " +
                    "email VARCHAR(100), " +
                    "phone VARCHAR(20), " +
                    "address TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");");

            // Accounts Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "user_id INT, " +
                    "account_number VARCHAR(20) UNIQUE NOT NULL, " +
                    "pin VARCHAR(4) NOT NULL, " +
                    "balance DECIMAL(15, 2) DEFAULT 0.00, " +
                    "account_type VARCHAR(20), " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ");");

            // Transactions Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "account_id INT, " +
                    "type VARCHAR(20), " +
                    "amount DECIMAL(15, 2), " +
                    "description TEXT, " +
                    "target_account_id INT, " +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE" +
                    ");");

            // Loans Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS loans (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "user_id INT, " +
                    "amount DECIMAL(15, 2), " +
                    "interest_rate DECIMAL(5, 2), " +
                    "term_months INT, " +
                    "status VARCHAR(20) DEFAULT 'PENDING', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ");");

            System.out.println("Database and tables checked/created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
