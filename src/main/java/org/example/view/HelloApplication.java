package org.example.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.service.DatabaseConnection;
import org.example.dao.UserDAO;
import org.example.view.Login.LoginView;

import java.sql.Connection;
import java.sql.SQLException;

public class HelloApplication extends Application {
    private static Connection connection;
    private static UserDAO userDAO;

    public static void main(String[] args) {
        try {
            // Establish database connection
            connection = DatabaseConnection.getConnection();
            if (connection == null) {
                System.err.println("Failed to connect to the database. Exiting.");
                System.exit(1); // Exit if the connection is null
            }

            // Initialize UserDAO
            userDAO = new UserDAO(connection);

            // Launch the JavaFX application
            launch(args);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection error: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create an instance of LoginView and pass the DAO
            LoginView loginView = new LoginView(userDAO);

            // Get the scene from LoginView and set it to the stage
            Scene scene = loginView.getScene();

            // Set up the stage
            primaryStage.setTitle("Museum Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing application: " + e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        // Close the database connection when the application stops
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        super.stop();
    }
}
