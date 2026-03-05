package com.banking.controller;

import com.banking.db.DataStore;
import com.banking.model.Account;
import com.banking.model.User;
import com.banking.util.Logger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    public List<String[]> getAllUsers() {
        List<String[]> res = new ArrayList<>();
        for (User u : DataStore.getUsers()) res.add(new String[]{String.valueOf(u.getId()), u.getUsername(), u.getRole(), u.getName(), u.getEmail()});
        return res;
    }

    public boolean deleteUser(int userId) {
        List<User> users = DataStore.getUsers();
        if (users.removeIf(u -> u.getId() == userId)) {
            DataStore.saveUsers(users);
            List<Account> accounts = DataStore.getAccounts();
            accounts.removeIf(a -> a.getUserId() == userId);
            DataStore.saveAccounts(accounts);
            Logger.info("User Deleted: " + userId);
            return true;
        }
        return false;
    }

    public boolean updateUserRole(int userId, String role) {
        List<User> users = DataStore.getUsers();
        for (User u : users) if (u.getId() == userId) { u.setRole(role); DataStore.saveUsers(users); return true; }
        return false;
    }

    public int createUser(String user, String pass, String role, String name, String email) {
        List<User> users = DataStore.getUsers();
        int id = users.isEmpty() ? 1 : users.get(users.size() - 1).getId() + 1;
        users.add(new User(id, user, pass, role, name, email, "", "", new Timestamp(System.currentTimeMillis())));
        DataStore.saveUsers(users);
        return id;
    }

    public boolean createAccount(int userId, String accNum, String pin) {
        List<Account> accs = DataStore.getAccounts();
        int id = accs.isEmpty() ? 1 : accs.get(accs.size() - 1).getId() + 1;
        accs.add(new Account(id, userId, accNum, pin, BigDecimal.ZERO, "SAVINGS", "ACTIVE", new Timestamp(System.currentTimeMillis())));
        DataStore.saveAccounts(accs);
        return true;
    }

    public String[] getStatistics() {
        List<Account> accs = DataStore.getAccounts();
        BigDecimal total = BigDecimal.ZERO;
        for (Account a : accs) total = total.add(a.getBalance());
        return new String[]{String.valueOf(DataStore.getUsers().size()), total.toString(), String.valueOf(DataStore.getTransactions().size())};
    }
}
