package org.example.service;

import java.sql.*;

public class DatabaseConnection {
    private static final String url = "jdbc:postgresql://10.200.10.163:5444/museum";
    private static final String user = "postgres";
    private static final String password = "museum";

    public Connection autoConnect() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(url,user,password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}

