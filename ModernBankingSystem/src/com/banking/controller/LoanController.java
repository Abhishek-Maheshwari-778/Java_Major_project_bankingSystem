package com.banking.controller;

import com.banking.db.DataStore;
import com.banking.model.Loan;
import com.banking.model.User;
import com.banking.util.Logger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LoanController {
    public boolean applyForLoan(int userId, BigDecimal amount, BigDecimal rate, int months) {
        List<Loan> loans = DataStore.getLoans();
        int id = loans.isEmpty() ? 1 : loans.get(loans.size() - 1).getId() + 1;
        loans.add(new Loan(id, userId, amount, rate, months, "PENDING", new Timestamp(System.currentTimeMillis())));
        DataStore.saveLoans(loans);
        Logger.info("Loan applied: User " + userId + ", Amount " + amount);
        return true;
    }

    public List<String[]> getLoansByUser(int userId) {
        List<String[]> res = new ArrayList<>();
        for (Loan l : DataStore.getLoans()) if (l.getUserId() == userId) res.add(new String[]{String.valueOf(l.getId()), l.getAmount().toString(), l.getStatus(), l.getCreatedAt().toString()});
        return res;
    }

    public List<String[]> getAllLoans() {
        List<String[]> res = new ArrayList<>();
        List<User> users = DataStore.getUsers();
        for (Loan l : DataStore.getLoans()) {
            String name = "Unknown";
            for (User u : users) if (u.getId() == l.getUserId()) { name = u.getUsername(); break; }
            res.add(new String[]{String.valueOf(l.getId()), name, l.getAmount().toString(), l.getInterestRate() + "%", String.valueOf(l.getTermMonths()), l.getStatus(), l.getCreatedAt().toString()});
        }
        return res;
    }

    public boolean updateLoanStatus(int loanId, String status) {
        List<Loan> loans = DataStore.getLoans();
        for (Loan l : loans) if (l.getId() == loanId) { l.setStatus(status); DataStore.saveLoans(loans); return true; }
        return false;
    }

    public boolean repayLoan(int loanId, int accId, BigDecimal amount) {
        if (new BankingController().withdraw(accId, amount, "Repay Loan " + loanId)) {
            List<Loan> loans = DataStore.getLoans();
            for (Loan l : loans) if (l.getId() == loanId) {
                l.setAmount(l.getAmount().subtract(amount));
                if (l.getAmount().compareTo(BigDecimal.ZERO) <= 0) l.setStatus("PAID");
                DataStore.saveLoans(loans);
                return true;
            }
        }
        return false;
    }
}
