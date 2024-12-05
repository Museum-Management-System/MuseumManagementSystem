package org.example.controller;

import org.example.dao.UserDAO;
import org.example.view.LoginView;
import org.example.view.MuseumArtifactView;
import org.example.service.MuseumArtifactService;
import org.example.dao.MuseumArtifactDAO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

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
                MuseumArtifactView artifactView = new MuseumArtifactView();
                artifactView.setVisible(true);

                // Initialize the artifact service and controller
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school", "postgres", "");
                MuseumArtifactDAO artifactDAO = new MuseumArtifactDAO(connection);
                MuseumArtifactService artifactService = new MuseumArtifactService(artifactDAO);
                MuseumArtifactController artifactController = new MuseumArtifactController(artifactView, artifactService);
            }
        } catch (NumberFormatException ex) {
            loginView.setMessage("Invalid User ID format.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
