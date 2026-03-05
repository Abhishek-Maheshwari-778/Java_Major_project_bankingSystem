package com.banking.controller;

import com.banking.db.DataStore;
import com.banking.model.User;
import com.banking.util.Logger;
import java.util.List;

public class AuthController {
    private static User currentUser;

    public boolean login(String username, String password) {
        List<User> users = DataStore.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                Logger.info("Login Successful: " + username + " (" + user.getRole() + ")");
                return true;
            }
        }
        Logger.info("Login Failed: " + username);
        return false;
    }

    public static User getCurrentUser() { return currentUser; }
    public static void logout() { if (currentUser != null) Logger.info("Logout: " + currentUser.getUsername()); currentUser = null; }
    public boolean hasRole(String role) { return currentUser != null && currentUser.getRole().equalsIgnoreCase(role); }

    public static void refreshSession() {
        if (currentUser == null) return;
        for (User user : DataStore.getUsers()) {
            if (user.getId() == currentUser.getId()) { currentUser = user; break; }
        }
    }
}
