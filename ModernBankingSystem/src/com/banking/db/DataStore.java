package com.banking.db;

import com.banking.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String DATA_DIR = "ModernBankingSystem/data/";
    private static final String USERS_FILE = DATA_DIR + "users.dat";
    private static final String ACCOUNTS_FILE = DATA_DIR + "accounts.dat";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "transactions.dat";
    private static final String LOANS_FILE = DATA_DIR + "loans.dat";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    private static <T> void saveToFile(String filename, List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) { return new ArrayList<>(); }
    }

    public static List<User> getUsers() { return loadFromFile(USERS_FILE); }
    public static void saveUsers(List<User> users) { saveToFile(USERS_FILE, users); }
    public static List<Account> getAccounts() { return loadFromFile(ACCOUNTS_FILE); }
    public static void saveAccounts(List<Account> accounts) { saveToFile(ACCOUNTS_FILE, accounts); }
    public static List<Transaction> getTransactions() { return loadFromFile(TRANSACTIONS_FILE); }
    public static void saveTransactions(List<Transaction> transactions) { saveToFile(TRANSACTIONS_FILE, transactions); }
    public static List<Loan> getLoans() { return loadFromFile(LOANS_FILE); }
    public static void saveLoans(List<Loan> loans) { saveToFile(LOANS_FILE, loans); }
}
