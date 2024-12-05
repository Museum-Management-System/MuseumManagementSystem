package org.example.controller;

import org.example.dao.UserDAO;
import org.example.view.LoginView;
import org.example.view.MuseumArtifactView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView loginView;
    private UserDAO userDAO;

    public LoginController(LoginView loginView, UserDAO userDAO) {
        this.loginView = loginView;
        this.userDAO = userDAO;

        this.loginView.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        try {
            String userType = loginView.getUserType();
            if ("Visitor".equals(userType)) {
                loginView.setMessage("Visitors are read-only users.");
                return;
            }

            int userId = loginView.getUserId();
            String password = loginView.getPassword();

            String role = userDAO.authenticateUser(userId, password);
            if (role == null) {
                loginView.setMessage("Invalid credentials. Try again.");
                return;
            }

            loginView.setMessage("Login successful! Redirecting...");
            loginView.dispose(); // Close the login view

            // Open the MuseumArtifactView for authorized users (Admin/Employee)
            if ("Admin".equals(role) || "Employee".equals(role)) {
                new MuseumArtifactView().setVisible(true); // Show the Artifact View
            }
        } catch (NumberFormatException ex) {
            loginView.setMessage("Invalid User ID format.");
        }
    }
}
