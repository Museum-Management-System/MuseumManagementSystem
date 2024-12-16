package org.example.view.Login;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import org.example.dao.UserDAO;

public class LoginView {
    private final UserDAO userDAO;

    public LoginView(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Scene getScene() {
        // Create UI components
        Label userIDLabel = new Label("User ID:");
        TextField userIDField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");

        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add components to layout
        gridPane.add(userIDLabel, 0, 0);
        gridPane.add(userIDField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);

        // Set login button action
        loginButton.setOnAction(event -> {
            try {
                int userID = Integer.parseInt(userIDField.getText());
                String password = passwordField.getText();

                // Authenticate user
                String role = userDAO.authenticateUser(userID, password);
                if (role != null) {
                    // Handle role-specific logic or navigation
                    System.out.println("Login successful! User role: " + role);
                } else {
                    showAlert("Login Failed", "Invalid user ID or password.");
                }
            } catch (NumberFormatException e) {
                showAlert("Input Error", "User ID must be a number.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An unexpected error occurred.");
            }
        });

        // Return the scene
        return new Scene(gridPane, 300, 200);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
