package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.example.dao.UserDAO;

public class LoginController {
    private static UserDAO userDAO; // Reference to the DAO

    // FXML Elements
    @FXML
    private TextField userIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    // Setter to receive the DAO from the application
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @FXML
    private void handleLogin() {
        try {
            // Read input from the text fields
            int userId = Integer.parseInt(userIdField.getText());
            String password = passwordField.getText();

            // Authenticate the user using the DAO
            String userType = userDAO.authenticateUser(userId, password);

            if (userType == null) {
                // Display error if authentication fails
                errorLabel.setText("Invalid credentials. Please try again.");
            } else {
                // Handle user type and redirect accordingly
                errorLabel.setText(""); // Clear any previous error message
                switch (userType) {
                    case "Admin":
                        loadAdminInterface();
                        break;
                    case "Employee":
                        loadEmployeeInterface();
                        break;
                    case "Guest":
                        loadGuestInterface();
                        break;
                    default:
                        errorLabel.setText("Unknown user type.");
                }
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid user ID format.");
        } catch (Exception e) {
            errorLabel.setText("An error occurred. Please try again.");
            e.printStackTrace();
        }
    }
    public String authenticateUser(int userID, String password) {
        // Delegate authentication logic to UserDAO
        return userDAO.authenticateUser(userID, password);
    }
    private void loadAdminInterface() {
        System.out.println("Load Admin Interface");
        // Implement scene switch to Admin interface
    }

    private void loadEmployeeInterface() {
        System.out.println("Load Employee Interface");
        // Implement scene switch to Employee interface
    }

    private void loadGuestInterface() {
        System.out.println("Load Guest Interface");
        // Implement scene switch to Guest interface
    }
}
