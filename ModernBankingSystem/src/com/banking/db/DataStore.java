package com.banking.db;

import com.banking.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    
    // USERS
    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer createdBy = rs.getObject("created_by") == null ? null : rs.getInt("created_by");
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at"),
                    createdBy
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return users;
    }

    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static void saveUsers(List<User> users) {
        // This is a bulk sync - for SQL we'll use UPSERT or check existence
        for (User u : users) {
            saveUser(u);
        }
    }

    private static void saveUser(User u) {
        String sql = "INSERT INTO users (id, username, password, role, name, email, phone, address, created_at, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE password=?, role=?, name=?, email=?, phone=?, address=?, created_by=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, u.getId());
            pstmt.setString(2, u.getUsername());
            pstmt.setString(3, u.getPassword());
            pstmt.setString(4, u.getRole());
            pstmt.setString(5, u.getName());
            pstmt.setString(6, u.getEmail());
            pstmt.setString(7, u.getPhone());
            pstmt.setString(8, u.getAddress());
            pstmt.setTimestamp(9, u.getCreatedAt());
            if (u.getCreatedBy() != null) pstmt.setInt(10, u.getCreatedBy()); else pstmt.setNull(10, Types.INTEGER);
            
            pstmt.setString(11, u.getPassword());
            pstmt.setString(12, u.getRole());
            pstmt.setString(13, u.getName());
            pstmt.setString(14, u.getEmail());
            pstmt.setString(15, u.getPhone());
            pstmt.setString(16, u.getAddress());
            if (u.getCreatedBy() != null) pstmt.setInt(17, u.getCreatedBy()); else pstmt.setNull(17, Types.INTEGER);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public static User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Integer createdBy = rs.getObject("created_by") == null ? null : rs.getInt("created_by");
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at"),
                        createdBy
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ACCOUNTS
    public static List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(new Account(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("account_number"),
                    rs.getString("pin"),
                    rs.getBigDecimal("balance"),
                    rs.getString("account_type"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return accounts;
    }

    public static void saveAccounts(List<Account> accounts) {
        for (Account a : accounts) {
            saveAccount(a);
        }
    }

    private static void saveAccount(Account a) {
        String sql = "INSERT INTO accounts (id, user_id, account_number, pin, balance, account_type, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE pin=?, balance=?, status=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, a.getId());
            pstmt.setInt(2, a.getUserId());
            pstmt.setString(3, a.getAccountNumber());
            pstmt.setString(4, a.getPin());
            pstmt.setBigDecimal(5, a.getBalance());
            pstmt.setString(6, a.getAccountType());
            pstmt.setString(7, a.getStatus());
            pstmt.setTimestamp(8, a.getCreatedAt());
            
            pstmt.setString(9, a.getPin());
            pstmt.setBigDecimal(10, a.getBalance());
            pstmt.setString(11, a.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // TRANSACTIONS
    public static List<Transaction> getTransactions() {
        List<Transaction> txs = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer targetId = rs.getInt("target_account_id");
                if (rs.wasNull()) targetId = null;
                txs.add(new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getString("type"),
                    rs.getBigDecimal("amount"),
                    rs.getString("description"),
                    targetId,
                    rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return txs;
    }

    public static void saveTransactions(List<Transaction> transactions) {
        for (Transaction t : transactions) {
            saveTransaction(t);
        }
    }

    private static void saveTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (id, account_id, type, amount, description, target_account_id, timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE type=type"; // Basically do nothing if exists
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, t.getId());
            pstmt.setInt(2, t.getAccountId());
            pstmt.setString(3, t.getType());
            pstmt.setBigDecimal(4, t.getAmount());
            pstmt.setString(5, t.getDescription());
            if (t.getTargetAccountId() != null) pstmt.setInt(6, t.getTargetAccountId());
            else pstmt.setNull(6, Types.INTEGER);
            pstmt.setTimestamp(7, t.getTimestamp());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // LOANS
    public static List<Loan> getLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("amount"),
                    rs.getBigDecimal("interest_rate"),
                    rs.getInt("term_months"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return loans;
    }

    public static void saveLoans(List<Loan> loans) {
        for (Loan l : loans) {
            saveLoan(l);
        }
    }

    private static void saveLoan(Loan l) {
        String sql = "INSERT INTO loans (id, user_id, amount, interest_rate, term_months, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE status=?, amount=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, l.getId());
            pstmt.setInt(2, l.getUserId());
            pstmt.setBigDecimal(3, l.getAmount());
            pstmt.setBigDecimal(4, l.getInterestRate());
            pstmt.setInt(5, l.getTermMonths());
            pstmt.setString(6, l.getStatus());
            pstmt.setTimestamp(7, l.getCreatedAt());
            
            pstmt.setString(8, l.getStatus());
            pstmt.setBigDecimal(9, l.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
