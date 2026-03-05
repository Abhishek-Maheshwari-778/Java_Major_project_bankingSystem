package com.banking.controller;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.util.Logger;
import java.math.BigDecimal;
import java.util.List;

public class ATMController {
    private BankingController bankingController = new BankingController();
    private Account currentAccount;
    private int loginAttempts = 0;

    public boolean validatePin(String accNum, String pin) {
        Account acc = bankingController.getAccountByNumber(accNum);
        if (acc == null || acc.getStatus().equals("BLOCKED")) return false;
        if (acc.getPin().equals(pin)) { currentAccount = acc; loginAttempts = 0; return true; }
        if (++loginAttempts >= 3) bankingController.updateAccountStatus(acc.getId(), "BLOCKED");
        return false;
    }

    public BigDecimal checkBalance() {
        if (currentAccount == null) return BigDecimal.ZERO;
        currentAccount = bankingController.getAccountByNumber(currentAccount.getAccountNumber());
        return currentAccount.getBalance();
    }

    public boolean withdrawCash(BigDecimal amt) {
        boolean ok = bankingController.withdraw(currentAccount.getId(), amt, "ATM Withdraw");
        if (ok) currentAccount = bankingController.getAccountByNumber(currentAccount.getAccountNumber());
        return ok;
    }

    public boolean depositCash(BigDecimal amt) {
        boolean ok = bankingController.deposit(currentAccount.getId(), amt, "ATM Deposit");
        if (ok) currentAccount = bankingController.getAccountByNumber(currentAccount.getAccountNumber());
        return ok;
    }

    public boolean changePin(String old, String next) {
        if (currentAccount.getPin().equals(old)) return bankingController.updatePin(currentAccount.getId(), next);
        return false;
    }

    public List<Transaction> getMiniStatement() { return bankingController.getTransactionHistory(currentAccount.getId()); }
    public void logout() { currentAccount = null; }
}
