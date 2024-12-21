package org.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://10.200.10.163:5444/museum";
    private static final String USER = "postgres";
    private static final String PASSWORD = "museum";

    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    /**
     * Establishes a connection to the database if not already connected.
     *
     * @return a Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to close the connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Testing the connection
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Connection failed: " + e.getMessage());
        } finally {
            closeConnection(); // Ensure connection is closed after testing
        }
    }
}
