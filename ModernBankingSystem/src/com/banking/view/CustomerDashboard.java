package com.banking.view;

import com.banking.controller.AuthController;
import com.banking.controller.BankingController;
import com.banking.controller.LoanController;
import com.banking.controller.UserController;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class CustomerDashboard extends BaseFrame {
    private BankingController bankingController = new BankingController();
    private LoanController loanController = new LoanController();
    private UserController userController = new UserController();
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentArea = new JPanel(cardLayout);

    public CustomerDashboard() {
        super("Customer Dashboard");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel sidebar = createSidebar("CUSTOMER PORTAL");
        JButton aBtn = createSidebarButton("  Accounts");
        JButton tBtn = createSidebarButton("  Transfer");
        JButton lBtn = createSidebarButton("  Loans");
        JButton hBtn = createSidebarButton("  History");
        JButton pBtn = createSidebarButton("  Profile");
        JButton out = createSidebarButton("  Logout");
        out.setForeground(ACCENT_COLOR);
        sidebar.add(aBtn); sidebar.add(tBtn); sidebar.add(lBtn); sidebar.add(hBtn); sidebar.add(pBtn); sidebar.add(Box.createVerticalGlue()); sidebar.add(out);
        add(sidebar, BorderLayout.WEST);
        contentArea.add(createAccountsPanel(), "ACCOUNTS");
        contentArea.add(createTransferPanel(), "TRANSFER");
        contentArea.add(createLoansPanel(), "LOANS");
        contentArea.add(createHistoryPanel(), "HISTORY");
        contentArea.add(createProfilePanel(), "PROFILE");
        add(contentArea, BorderLayout.CENTER);
        add(createHeader("Welcome, " + AuthController.getCurrentUser().getName()), BorderLayout.NORTH);
        aBtn.addActionListener(e -> cardLayout.show(contentArea, "ACCOUNTS"));
        tBtn.addActionListener(e -> cardLayout.show(contentArea, "TRANSFER"));
        lBtn.addActionListener(e -> cardLayout.show(contentArea, "LOANS"));
        hBtn.addActionListener(e -> cardLayout.show(contentArea, "HISTORY"));
        pBtn.addActionListener(e -> cardLayout.show(contentArea, "PROFILE"));
        out.addActionListener(e -> { AuthController.logout(); dispose(); new LoginScreen().setVisible(true); });
    }

    private JPanel createAccountsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] cols = {"Acc Num", "Type", "Balance", "Status"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        JTable t = new JTable(m);
        for (Account a : bankingController.getAccountsByUserId(AuthController.getCurrentUser().getId())) m.addRow(new Object[]{a.getAccountNumber(), a.getAccountType(), a.getBalance(), a.getStatus()});
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    private JPanel createTransferPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        JTextField to = new JTextField(15); JTextField am = new JTextField(15); JButton b = new JButton("Transfer");
        styleButton(b);
        g.gridx = 0; g.gridy = 0; p.add(new JLabel("To Acc:"), g);
        g.gridx = 1; p.add(to, g);
        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Amount:"), g);
        g.gridx = 1; p.add(am, g);
        g.gridy = 2; p.add(b, g);
        b.addActionListener(e -> {
            List<Account> accs = bankingController.getAccountsByUserId(AuthController.getCurrentUser().getId());
            if (!accs.isEmpty() && bankingController.transfer(accs.get(0).getId(), to.getText(), new BigDecimal(am.getText()), "Transfer")) JOptionPane.showMessageDialog(this, "Success");
            else JOptionPane.showMessageDialog(this, "Failed");
        });
        return p;
    }

    private JPanel createLoansPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        DefaultTableModel m = new DefaultTableModel(new String[]{"ID", "Amt", "Status", "Date"}, 0);
        JTable t = new JTable(m);
        for (String[] row : loanController.getLoansByUser(AuthController.getCurrentUser().getId())) m.addRow(row);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        JButton app = new JButton("Apply Loan"); styleButton(app);
        app.addActionListener(e -> { String s = JOptionPane.showInputDialog("Amt:"); if (s != null && loanController.applyForLoan(AuthController.getCurrentUser().getId(), new BigDecimal(s), new BigDecimal("10.5"), 12)) JOptionPane.showMessageDialog(this, "Applied"); });
        p.add(app, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        DefaultTableModel m = new DefaultTableModel(new String[]{"Date", "Type", "Amt", "Desc"}, 0);
        JTable t = new JTable(m);
        List<Account> accs = bankingController.getAccountsByUserId(AuthController.getCurrentUser().getId());
        if (!accs.isEmpty()) for (Transaction tx : bankingController.getTransactionHistory(accs.get(0).getId())) m.addRow(new Object[]{tx.getTimestamp(), tx.getType(), tx.getAmount(), tx.getDescription()});
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        User u = AuthController.getCurrentUser();
        JTextField nm = new JTextField(u.getName(), 15); JButton b = new JButton("Update"); styleButton(b);
        p.add(new JLabel("Name:")); p.add(nm); p.add(b);
        b.addActionListener(e -> { if (userController.updateProfile(u.getId(), nm.getText(), u.getEmail(), u.getPhone(), u.getAddress())) JOptionPane.showMessageDialog(this, "Updated"); });
        return p;
    }
}
