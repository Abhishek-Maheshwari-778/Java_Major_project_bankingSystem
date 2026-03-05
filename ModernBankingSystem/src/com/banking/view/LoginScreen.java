package com.banking.view;

import com.banking.controller.AuthController;
import com.banking.db.DatabaseSeeder;
import com.banking.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends BaseFrame {
    private JTextField userField;
    private JPasswordField passField;
    private AuthController authController = new AuthController();

    public LoginScreen() {
        super("Login");
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel logo = new JLabel("MODERN BANK");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logo.setForeground(PRIMARY_COLOR);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        userField = new JTextField(20);
        userField.setMaximumSize(new Dimension(300, 40));
        userField.setBorder(BorderFactory.createTitledBorder("Username"));

        passField = new JPasswordField(20);
        passField.setMaximumSize(new Dimension(300, 40));
        passField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginBtn = new JButton("LOGIN");
        styleButton(loginBtn);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(300, 45));
        loginBtn.addActionListener(e -> handleLogin());

        JButton atmBtn = new JButton("Open ATM Simulator");
        atmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        atmBtn.setForeground(PRIMARY_COLOR);
        atmBtn.setContentAreaFilled(false);
        atmBtn.addActionListener(e -> { dispose(); new ATMScreen().setVisible(true); });

        p.add(logo); p.add(Box.createVerticalStrut(30));
        p.add(userField); p.add(Box.createVerticalStrut(15));
        p.add(passField); p.add(Box.createVerticalStrut(25));
        p.add(loginBtn); p.add(Box.createVerticalStrut(15));
        p.add(atmBtn);
        add(p);
    }

    private void handleLogin() {
        if (authController.login(userField.getText(), new String(passField.getPassword()))) {
            User u = AuthController.getCurrentUser();
            dispose();
            if (u.getRole().equals("ADMIN")) new AdminDashboard().setVisible(true);
            else if (u.getRole().equals("EMPLOYEE")) new EmployeeDashboard().setVisible(true);
            else new CustomerDashboard().setVisible(true);
        } else JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        DatabaseSeeder.seed();
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
