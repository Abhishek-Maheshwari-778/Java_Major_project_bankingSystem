package com.banking.db;

import com.banking.model.Account;
import com.banking.model.User;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class DatabaseSeeder {
    public static void seed() {
        List<User> users = DataStore.getUsers();
        if (users.isEmpty()) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            users.add(new User(1, "admin", "admin123", "SUPER_ADMIN", "System Admin", "admin@bank.com", "", "", now, null));
            users.add(new User(2, "emp01", "emp123", "EMPLOYEE", "John Teller", "john@bank.com", "", "", now));
            users.add(new User(3, "user01", "pass123", "CUSTOMER", "Alice Smith", "alice@example.com", "", "", now));
            DataStore.saveUsers(users);

            List<Account> accounts = DataStore.getAccounts();
            accounts.add(new Account(1, 3, "1000100", "1234", new BigDecimal("5000.00"), "SAVINGS", "ACTIVE", now));
            DataStore.saveAccounts(accounts);
            System.out.println("Data seeded successfully.");
        }
    }
}
