package com.banking.view;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame {
    protected static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    protected static final Color SECONDARY_COLOR = new Color(44, 62, 80);
    protected static final Color ACCENT_COLOR = new Color(231, 76, 60);
    protected static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    protected static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public BaseFrame(String title) {
        setTitle(title + " - Modern Banking System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    protected void styleButton(JButton btn) {
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY_COLOR.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY_COLOR); }
        });
    }

    protected JPanel createHeader(String text) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SECONDARY_COLOR);
        header.setPreferredSize(new Dimension(1000, 70));
        JLabel title = new JLabel("  " + text);
        title.setForeground(Color.WHITE);
        title.setFont(TITLE_FONT);
        header.add(title, BorderLayout.WEST);

        JButton aboutBtn = new JButton("About Project ");
        aboutBtn.setForeground(Color.WHITE);
        aboutBtn.setContentAreaFilled(false);
        aboutBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        aboutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutBtn.addActionListener(e -> showAboutDialog());
        header.add(aboutBtn, BorderLayout.EAST);

        return header;
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "About Modern Banking System", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        p.setBackground(Color.WHITE);

        JLabel title = new JLabel("Modern Banking System v1.0");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea desc = new JTextArea("A comprehensive banking application built with Java Swing and MySQL, " +
                "featuring secure transactions, loan management, and role-based access control.");
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setEditable(false);
        desc.setFont(NORMAL_FONT);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setMaximumSize(new Dimension(400, 80));

        JLabel teamHeader = new JLabel("Team Members:");
        teamHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        teamHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        String teamInfo = "<html><center>" +
                "<b>Govind Gupta</b><br>GitHub: Govind-gupta243<br><br>" +
                "<b>Khushi Gupta</b><br>GitHub: happycode290<br><br>" +
                "<b>Pallal</b><br>GitHub: pallal" +
                "</center></html>";
        JLabel team = new JLabel(teamInfo);
        team.setFont(NORMAL_FONT);
        team.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton close = new JButton("Close");
        styleButton(close);
        close.setAlignmentX(Component.CENTER_ALIGNMENT);
        close.addActionListener(e -> dialog.dispose());

        p.add(title); p.add(Box.createVerticalStrut(15));
        p.add(desc); p.add(Box.createVerticalStrut(20));
        p.add(teamHeader); p.add(Box.createVerticalStrut(10));
        p.add(team); p.add(Box.createVerticalStrut(25));
        p.add(close);

        dialog.add(p);
        dialog.setVisible(true);
    }

    protected JPanel createSidebar(String role) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SECONDARY_COLOR);
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        JLabel brand = new JLabel("MODERN BANK");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(brand);
        JLabel rl = new JLabel(role);
        rl.setForeground(Color.LIGHT_GRAY);
        rl.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(rl);
        sidebar.add(Box.createVerticalStrut(40));
        return sidebar;
    }

    protected JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(SECONDARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY_COLOR); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(SECONDARY_COLOR); }
        });
        return btn;
    }
}
