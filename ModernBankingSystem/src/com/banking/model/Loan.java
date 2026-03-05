package com.banking.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int userId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private int termMonths;
    private String status;
    private Timestamp createdAt;

    public Loan(int id, int userId, BigDecimal amount, BigDecimal interestRate, int termMonths, String status, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public int getTermMonths() { return termMonths; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
}
