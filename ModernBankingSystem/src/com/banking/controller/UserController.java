package com.banking.controller;

import com.banking.db.DataStore;
import com.banking.model.User;
import com.banking.util.Logger;
import java.util.List;

public class UserController {
    public boolean updateProfile(int userId, String name, String email, String phone, String address) {
        List<User> users = DataStore.getUsers();
        for (User u : users) if (u.getId() == userId) {
            u.setName(name); u.setEmail(email); u.setPhone(phone); u.setAddress(address);
            DataStore.saveUsers(users);
            Logger.info("Profile updated: " + userId);
            return true;
        }
        return false;
    }

    public boolean changePassword(int userId, String oldPass, String newPass) {
        List<User> users = DataStore.getUsers();
        for (User u : users) if (u.getId() == userId) {
            if (u.getPassword().equals(oldPass)) { u.setPassword(newPass); DataStore.saveUsers(users); return true; }
            break;
        }
        return false;
    }
}
