package org.example.view.Login;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import org.example.dao.UserDAO;
import org.example.view.GUIs.AdminGUI;
import org.example.view.GUIs.EmployeeGUI;
import org.example.view.GUIs.GuestUserGUI;

public class LoginView {
    private final UserDAO userDAO;

    public LoginView(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Scene getScene(Stage primaryStage) {
        // UI components for buttons
        Button authenticatedUserButton = new Button("Authenticated User");
        Button guestUserButton = new Button("       Guest User       ");

        // Style buttons
        String buttonStyle = "-fx-background-color: #ff4d4d; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 2;";
        authenticatedUserButton.setStyle(buttonStyle);
        guestUserButton.setStyle(buttonStyle);

        // UI components for login fields (initially hidden)
        Label userIDLabel = new Label("User ID:");
        TextField userIDField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setStyle(buttonStyle);

        // Style labels
        userIDLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        // Title label
        Label titleLabel = new Label("Museum Management System");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web("#4A4A4A"));

        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#d3d3d3"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Add components (default screen)
        gridPane.add(titleLabel, 0, 0, 4, 1);
        gridPane.add(authenticatedUserButton, 2, 2);
        gridPane.add(guestUserButton, 2, 3);

        // Add login components (hidden by default)
        gridPane.add(userIDLabel, 1, 2);
        gridPane.add(userIDField, 2, 2);
        gridPane.add(passwordLabel, 1, 3);
        gridPane.add(passwordField, 2, 3);
        gridPane.add(loginButton, 2, 4);

        // Hide login fields initially
        userIDLabel.setVisible(false);
        userIDField.setVisible(false);
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        loginButton.setVisible(false);

        // Action for Guest User Button
        guestUserButton.setOnAction(event -> {
            primaryStage.close(); // Close the current window
            new GuestUserGUI().start(new Stage()); // Open GuestUser GUI
        });

        // Action for Authenticated User Button
        authenticatedUserButton.setOnAction(event -> {
            // Hide buttons and show login fields
            authenticatedUserButton.setVisible(false);
            guestUserButton.setVisible(false);

            userIDLabel.setVisible(true);
            userIDField.setVisible(true);
            passwordLabel.setVisible(true);
            passwordField.setVisible(true);
            loginButton.setVisible(true);
        });

        // Action for Login Button
        loginButton.setOnAction(event -> {
            try {
                int userID = Integer.parseInt(userIDField.getText());
                String password = passwordField.getText();

                handleLogin(userID, password, primaryStage);
            } catch (NumberFormatException e) {
                showAlert("Input Error", "User ID must be a number.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An unexpected error occurred.");
            }
        });

        // Return the scene
        Scene scene=new Scene(gridPane, 380, 250);
        primaryStage.setResizable(false);
        return scene;
    }

    private void handleLogin(int userID, String password, Stage primaryStage) {
        try {
            String role = userDAO.authenticateUser(userID, password);
            if (role != null) {
                openInterface(role, primaryStage);
            } else {
                showAlert("Login Failed", "Invalid user ID or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred.");
        }
    }

    private void openInterface(String role, Stage primaryStage) {
        primaryStage.close(); // Close login window
        switch (role) {
            case "Employee":
                new EmployeeGUI().start(new Stage());
                break;
            case "Admin":
                new AdminGUI().start(new Stage());
                break;
            default:
                new GuestUserGUI().start(new Stage());
                break;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
