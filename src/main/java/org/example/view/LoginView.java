package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JComboBox<String> userTypeComboBox;
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginView() {
        setTitle("Museum Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        loginPanel.add(new JLabel("Select User Type:"));
        userTypeComboBox = new JComboBox<>(new String[]{"Visitor", "Authorized User"});
        loginPanel.add(userTypeComboBox);

        loginPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        loginPanel.add(userIdField);

        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginPanel.add(loginButton);

        messageLabel = new JLabel("", JLabel.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        add(loginPanel, BorderLayout.CENTER);
    }

    public String getUserType() {
        return (String) userTypeComboBox.getSelectedItem();
    }

    public int getUserId() {
        return Integer.parseInt(userIdField.getText());
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
}

