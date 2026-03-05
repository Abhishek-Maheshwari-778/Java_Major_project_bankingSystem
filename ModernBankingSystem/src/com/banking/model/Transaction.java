package com.banking.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int accountId;
    private String type;
    private BigDecimal amount;
    private String description;
    private Integer targetAccountId;
    private Timestamp timestamp;

    public Transaction(int id, int accountId, String type, BigDecimal amount, String description, Integer targetAccountId, Timestamp timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public int getAccountId() { return accountId; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public Integer getTargetAccountId() { return targetAccountId; }
    public Timestamp getTimestamp() { return timestamp; }
}
