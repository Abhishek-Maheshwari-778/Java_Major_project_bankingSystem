package com.banking.view;

import com.banking.controller.ATMController;
import com.banking.model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ATMScreen extends BaseFrame {
    private ATMController atmController = new ATMController();
    private JTextField accField;
    private JPasswordField pinField;
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public ATMScreen() {
        super("ATM Simulator");
        initUI();
    }

    private void initUI() {
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createMenuPanel(), "MENU");
        add(cardPanel);
    }

    private JPanel createLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        JLabel title = new JLabel("ATM SIMULATOR");
        title.setFont(TITLE_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        accField = new JTextField(20);
        accField.setBorder(BorderFactory.createTitledBorder("Account Number"));
        pinField = new JPasswordField(20);
        pinField.setBorder(BorderFactory.createTitledBorder("ATM PIN"));
        JButton enter = new JButton("ENTER");
        styleButton(enter);
        enter.setAlignmentX(Component.CENTER_ALIGNMENT);
        enter.addActionListener(e -> {
            if (atmController.validatePin(accField.getText(), new String(pinField.getPassword()))) cardLayout.show(cardPanel, "MENU");
            else JOptionPane.showMessageDialog(this, "Invalid Account/PIN or BLOCKED");
        });
        box.add(title); box.add(Box.createVerticalStrut(30));
        box.add(accField); box.add(Box.createVerticalStrut(15));
        box.add(pinField); box.add(Box.createVerticalStrut(25));
        box.add(enter);
        p.add(box);
        return p;
    }

    private JPanel createMenuPanel() {
        JPanel p = new JPanel(new GridLayout(3, 2, 20, 20));
        p.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        JButton bal = new JButton("Balance");
        JButton with = new JButton("Withdraw");
        JButton dep = new JButton("Deposit");
        JButton mini = new JButton("Mini Statement");
        JButton pin = new JButton("Change PIN");
        JButton exit = new JButton("Exit");
        styleButton(bal); styleButton(with); styleButton(dep); styleButton(mini); styleButton(pin); styleButton(exit);
        exit.setBackground(ACCENT_COLOR);
        bal.addActionListener(e -> JOptionPane.showMessageDialog(this, "Balance: Rs. " + atmController.checkBalance()));
        with.addActionListener(e -> {
            String s = JOptionPane.showInputDialog("Amount:");
            if (s != null && atmController.withdrawCash(new BigDecimal(s))) JOptionPane.showMessageDialog(this, "Success");
            else JOptionPane.showMessageDialog(this, "Failed");
        });
        dep.addActionListener(e -> {
            String s = JOptionPane.showInputDialog("Amount:");
            if (s != null && atmController.depositCash(new BigDecimal(s))) JOptionPane.showMessageDialog(this, "Success");
        });
        mini.addActionListener(e -> {
            List<Transaction> txs = atmController.getMiniStatement();
            StringBuilder sb = new StringBuilder("Mini Statement:\n");
            for (int i = 0; i < Math.min(5, txs.size()); i++) sb.append(txs.get(i).getTimestamp()).append(" | ").append(txs.get(i).getType()).append(" | ").append(txs.get(i).getAmount()).append("\n");
            JOptionPane.showMessageDialog(this, new JTextArea(sb.toString()));
        });
        pin.addActionListener(e -> {
            String o = JOptionPane.showInputDialog("Old PIN:");
            String n = JOptionPane.showInputDialog("New PIN:");
            if (o != null && n != null && atmController.changePin(o, n)) JOptionPane.showMessageDialog(this, "Success");
        });
        exit.addActionListener(e -> { atmController.logout(); dispose(); new LoginScreen().setVisible(true); });
        p.add(bal); p.add(with); p.add(dep); p.add(mini); p.add(pin); p.add(exit);
        return p;
    }
}
