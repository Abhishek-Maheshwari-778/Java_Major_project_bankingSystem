package com.banking.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int userId;
    private String accountNumber;
    private String pin;
    private BigDecimal balance;
    private String accountType;
    private String status;
    private Timestamp createdAt;

    public Account(int id, int userId, String accountNumber, String pin, BigDecimal balance, String accountType, String status, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getAccountNumber() { return accountNumber; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getAccountType() { return accountType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
}
