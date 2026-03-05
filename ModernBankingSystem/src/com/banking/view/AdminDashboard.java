package com.banking.view;

import com.banking.controller.AdminController;
import com.banking.controller.AuthController;
import com.banking.controller.LoanController;
import com.banking.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends BaseFrame {
    private AdminController adminController = new AdminController();
    private LoanController loanController = new LoanController();
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentArea = new JPanel(cardLayout);

    public AdminDashboard() {
        super("Admin Dashboard");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel sidebar = createSidebar("ADMIN PORTAL");
        JButton uBtn = createSidebarButton("  Users");
        JButton lBtn = createSidebarButton("  Loans");
        JButton sBtn = createSidebarButton("  Stats");
        JButton out = createSidebarButton("  Logout");
        out.setForeground(ACCENT_COLOR);
        sidebar.add(uBtn); sidebar.add(lBtn); sidebar.add(sBtn); sidebar.add(Box.createVerticalGlue()); sidebar.add(out);
        add(sidebar, BorderLayout.WEST);
        contentArea.add(createUserPanel(), "USERS");
        contentArea.add(createLoanPanel(), "LOANS");
        contentArea.add(createStatsPanel(), "STATS");
        add(contentArea, BorderLayout.CENTER);
        add(createHeader("Admin: " + AuthController.getCurrentUser().getName()), BorderLayout.NORTH);
        uBtn.addActionListener(e -> cardLayout.show(contentArea, "USERS"));
        lBtn.addActionListener(e -> cardLayout.show(contentArea, "LOANS"));
        sBtn.addActionListener(e -> cardLayout.show(contentArea, "STATS"));
        out.addActionListener(e -> { AuthController.logout(); dispose(); new LoginScreen().setVisible(true); });
    }

    protected JPanel createUserPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] cols = {"ID", "User", "Role", "Name", "Email"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        JTable t = new JTable(m);
        for (String[] row : adminController.getAllUsers()) m.addRow(row);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        JButton del = new JButton("Delete User");
        styleButton(del);
        del.addActionListener(e -> {
            int r = t.getSelectedRow();
            if (r != -1 && adminController.deleteUser(Integer.parseInt((String) m.getValueAt(r, 0)))) m.removeRow(r);
        });
        p.add(del, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createLoanPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        String[] cols = {"ID", "User", "Amt", "Rate", "Term", "Status", "Date"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        JTable t = new JTable(m);
        for (String[] row : loanController.getAllLoans()) m.addRow(row);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        JPanel bp = new JPanel();
        JButton app = new JButton("Approve"); JButton rej = new JButton("Reject");
        styleButton(app); styleButton(rej); rej.setBackground(ACCENT_COLOR);
        app.addActionListener(e -> { int r = t.getSelectedRow(); if (r != -1) { loanController.updateLoanStatus(Integer.parseInt((String)m.getValueAt(r, 0)), "APPROVED"); m.setValueAt("APPROVED", r, 5); } });
        rej.addActionListener(e -> { int r = t.getSelectedRow(); if (r != -1) { loanController.updateLoanStatus(Integer.parseInt((String)m.getValueAt(r, 0)), "REJECTED"); m.setValueAt("REJECTED", r, 5); } });
        bp.add(app); bp.add(rej); p.add(bp, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createStatsPanel() {
        JPanel p = new JPanel(new GridLayout(4, 1, 20, 20));
        p.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        String[] s = adminController.getStatistics();
        p.add(new JLabel("Total Users: " + s[0]));
        p.add(new JLabel("Total Bank Balance: Rs. " + s[1]));
        p.add(new JLabel("Total Transactions: " + s[2]));
        
        JButton intBtn = new JButton("Apply Monthly Interest (4% Savings)");
        styleButton(intBtn);
        intBtn.addActionListener(e -> {
            new com.banking.controller.BankingController().applyInterest();
            JOptionPane.showMessageDialog(this, "Monthly interest applied to all active savings accounts.");
            cardLayout.show(contentArea, "STATS"); // Refresh
        });
        p.add(intBtn);
        return p;
    }
}
