package org.example;

import org.example.controller.LoginController;
import org.example.dao.UserDAO;
import org.example.view.LoginView;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQLConnection {
   /* private static final String URL = "jdbc:postgresql://localhost:5433/school";
    private static final String USER = "postgres"; // Replace with your PostgreSQL username
    private static final String PASSWORD = "museum"; // Replace with your PostgreSQL password*/

    public static void main(String[] args) {
       /* try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed.");
        }*/
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school", "postgres", "");

            // Initialize the User-related components (Login functionality)
            UserDAO userDAO = new UserDAO(connection);
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, userDAO);
            loginView.setVisible(true);

            // Initialize Artifact-related components (Artifact management functionality)
          /* MuseumArtifactDAO artifactDAO = new MuseumArtifactDAO(connection);
            MuseumArtifactService artifactService = new MuseumArtifactService(artifactDAO);
            MuseumArtifactView artifactView = new MuseumArtifactView();
            MuseumArtifactController artifactController = new MuseumArtifactController(artifactView, artifactService);

            // Make ArtifactView visible if required immediately
            artifactView.setVisible(true);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
