package com.banking.view;

import com.banking.controller.AdminController;
import com.banking.controller.AuthController;
import com.banking.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class EmployeeDashboard extends BaseFrame {
    private AdminController adminController = new AdminController();
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentArea = new JPanel(cardLayout);

    public EmployeeDashboard() {
        super("Employee Dashboard");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel sidebar = createSidebar("EMPLOYEE PORTAL");
        JButton cBtn = createSidebarButton("  New Customer");
        JButton vBtn = createSidebarButton("  View Customers");
        JButton out = createSidebarButton("  Logout");
        out.setForeground(ACCENT_COLOR);
        sidebar.add(cBtn); sidebar.add(vBtn); sidebar.add(Box.createVerticalGlue()); sidebar.add(out);
        add(sidebar, BorderLayout.WEST);

        contentArea.add(createHomePanel(), "HOME");
        contentArea.add(createCustomerListPanel(), "CUSTOMERS");
        add(contentArea, BorderLayout.CENTER);

        add(createHeader("Employee: " + AuthController.getCurrentUser().getName()), BorderLayout.NORTH);
        cBtn.addActionListener(e -> handleCreate());
        vBtn.addActionListener(e -> cardLayout.show(contentArea, "CUSTOMERS"));
        out.addActionListener(e -> { AuthController.logout(); dispose(); new LoginScreen().setVisible(true); });
    }

    private JPanel createHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.add(new JLabel("Select an action from the sidebar."));
        return p;
    }

    private JPanel createCustomerListPanel() {
        JPanel p = new AdminDashboard().createUserPanel(); // Reuse the user panel from AdminDashboard
        JButton backBtn = new JButton("Back to Dashboard");
        styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(contentArea, "HOME"));
        p.add(backBtn, BorderLayout.NORTH);
        return p;
    }

    private void handleCreate() {
        String u = JOptionPane.showInputDialog("Username:");
        String p = JOptionPane.showInputDialog("Password:");
        String n = JOptionPane.showInputDialog("Full Name:");
        String em = JOptionPane.showInputDialog("Email:");
        if (u != null && p != null) {
            int id = adminController.createUser(u, p, "CUSTOMER", n, em);
            String acc = "1000" + (1000 + new Random().nextInt(9000));
            String pin = "1234";
            adminController.createAccount(id, acc, pin);
            JOptionPane.showMessageDialog(this, "Customer Created\nAcc: " + acc + "\nPIN: " + pin);
        }
    }
}
