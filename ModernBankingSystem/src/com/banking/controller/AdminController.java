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
        for (User u : DataStore.getUsers()) {
            String createdBy = "";
            if (u.getCreatedBy() != null) {
                User creator = DataStore.getUserById(u.getCreatedBy());
                createdBy = creator != null ? creator.getUsername() : String.valueOf(u.getCreatedBy());
            }
            res.add(new String[]{String.valueOf(u.getId()), u.getUsername(), u.getRole(), u.getName(), u.getEmail(), createdBy});
        }
        return res;
    }

    public boolean deleteUser(int userId) {
        User target = DataStore.getUserById(userId);
        if (target != null && ("ADMIN".equalsIgnoreCase(target.getRole()) || "SUPER_ADMIN".equalsIgnoreCase(target.getRole()))) {
            Logger.warn("Attempt to delete admin user blocked: " + userId);
            return false;
        }
        boolean ok = DataStore.deleteUser(userId);
        if (ok) Logger.info("User Deleted: " + userId);
        return ok;
    }

    public boolean updateUserRole(int userId, String role) {
        User actor = AuthController.getCurrentUser();
        if (actor == null) return false;
        if ("SUPER_ADMIN".equalsIgnoreCase(role) && !"SUPER_ADMIN".equalsIgnoreCase(actor.getRole())) return false;
        List<User> users = DataStore.getUsers();
        for (User u : users) if (u.getId() == userId) {
            if ("SUPER_ADMIN".equalsIgnoreCase(u.getRole()) && !"SUPER_ADMIN".equalsIgnoreCase(actor.getRole())) return false;
            u.setRole(role); DataStore.saveUsers(users); return true; }
        return false;
    }

    public int createUser(String user, String pass, String role, String name, String email) {
        if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) return -1;
        User actor = AuthController.getCurrentUser();
        if (actor == null) return -1;
        if ("ADMIN".equalsIgnoreCase(role) && !"SUPER_ADMIN".equalsIgnoreCase(actor.getRole())) return -1;
        if ("SUPER_ADMIN".equalsIgnoreCase(role)) return -1;
        List<User> users = DataStore.getUsers();
        for (User u : users) if (u.getUsername().equals(user)) {
            Logger.warn("Create user failed: Username " + user + " already exists.");
            return -1;
        }
        int id = users.isEmpty() ? 1 : users.get(users.size() - 1).getId() + 1;
        User nu = new User(id, user, pass, role, name, email, "", "", new Timestamp(System.currentTimeMillis()));
        nu.setCreatedBy(actor.getId());
        users.add(nu);
        DataStore.saveUsers(users);
        return id;
    }

    public boolean createAccount(int userId, String accNum, String pin) {
        if (accNum == null || accNum.isEmpty() || pin == null || pin.length() != 4) return false;
        List<Account> accs = DataStore.getAccounts();
        for (Account a : accs) if (a.getAccountNumber().equals(accNum)) {
            Logger.warn("Create account failed: Account number " + accNum + " already exists.");
            return false;
        }
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
