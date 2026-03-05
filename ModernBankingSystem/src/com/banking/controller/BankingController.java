package com.banking.controller;

import com.banking.db.DataStore;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.util.Logger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BankingController {
    public Account getAccountByNumber(String accNum) {
        for (Account acc : DataStore.getAccounts()) if (acc.getAccountNumber().equals(accNum)) return acc;
        return null;
    }

    public boolean deposit(int accId, BigDecimal amount, String desc) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        List<Account> accounts = DataStore.getAccounts();
        for (Account acc : accounts) {
            if (acc.getId() == accId) {
                acc.setBalance(acc.getBalance().add(amount));
                DataStore.saveAccounts(accounts);
                recordTransaction(accId, "DEPOSIT", amount, desc, null);
                Logger.info("Deposit: " + amount + " to ID " + accId);
                return true;
            }
        }
        return false;
    }

    public boolean withdraw(int accId, BigDecimal amount, String desc) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        List<Account> accounts = DataStore.getAccounts();
        for (Account acc : accounts) {
            if (acc.getId() == accId) {
                if (acc.getBalance().compareTo(amount) >= 0) {
                    acc.setBalance(acc.getBalance().subtract(amount));
                    DataStore.saveAccounts(accounts);
                    recordTransaction(accId, "WITHDRAW", amount, desc, null);
                    Logger.info("Withdrawal: " + amount + " from ID " + accId);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private static final BigDecimal DAILY_LIMIT = new BigDecimal("50000.00");

    public boolean transfer(int fromId, String toAccNum, BigDecimal amount, String desc) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (amount.compareTo(DAILY_LIMIT) > 0) {
            Logger.warn("Transfer failed: Exceeds daily limit of " + DAILY_LIMIT);
            return false;
        }

        Account target = getAccountByNumber(toAccNum);
        if (target == null) return false;

        if (withdraw(fromId, amount, "Transfer to " + toAccNum + ": " + desc)) {
            deposit(target.getId(), amount, "Transfer from ID " + fromId);
            Logger.info("Transfer: " + amount + " from ID " + fromId + " to " + toAccNum);
            return true;
        }
        return false;
    }

    public void applyInterest() {
        List<Account> accounts = DataStore.getAccounts();
        BigDecimal rate = new BigDecimal("0.04"); // 4% Annual
        for (Account a : accounts) {
            if ("SAVINGS".equals(a.getAccountType()) && "ACTIVE".equals(a.getStatus())) {
                BigDecimal interest = a.getBalance().multiply(rate).divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);
                if (interest.compareTo(BigDecimal.ZERO) > 0) {
                    a.setBalance(a.getBalance().add(interest));
                    Logger.info("Interest applied to " + a.getAccountNumber() + ": " + interest);
                }
            }
        }
        DataStore.saveAccounts(accounts);
    }

    private void recordTransaction(int accId, String type, BigDecimal amount, String desc, Integer targetId) {
        List<Transaction> txs = DataStore.getTransactions();
        int maxId = 0;
        for (Transaction t : txs) if (t.getId() > maxId) maxId = t.getId();
        int nextId = maxId + 1;
        txs.add(new Transaction(nextId, accId, type, amount, desc, targetId, new Timestamp(System.currentTimeMillis())));
        DataStore.saveTransactions(txs);
    }

    public List<Account> getAccountsByUserId(int userId) {
        List<Account> res = new ArrayList<>();
        for (Account acc : DataStore.getAccounts()) if (acc.getUserId() == userId) res.add(acc);
        return res;
    }

    public List<Transaction> getTransactionHistory(int accId) {
        List<Transaction> res = new ArrayList<>();
        List<Transaction> all = DataStore.getTransactions();
        for (int i = all.size() - 1; i >= 0; i--) if (all.get(i).getAccountId() == accId) res.add(all.get(i));
        return res;
    }

    public boolean updateAccountStatus(int accId, String status) {
        List<Account> accounts = DataStore.getAccounts();
        for (Account acc : accounts) if (acc.getId() == accId) { acc.setStatus(status); DataStore.saveAccounts(accounts); return true; }
        return false;
    }

    public boolean updatePin(int accId, String newPin) {
        List<Account> accounts = DataStore.getAccounts();
        for (Account acc : accounts) if (acc.getId() == accId) { acc.setPin(newPin); DataStore.saveAccounts(accounts); return true; }
        return false;
    }
}
