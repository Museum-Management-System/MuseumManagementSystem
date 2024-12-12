package org.example.dao;

import org.example.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public String authenticateUser(int userId, String password) {
        String sql = "SELECT user_type FROM users WHERE user_id = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void insertUser(User user) {
        String sql = "INSERT INTO users (user_id, user_type, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user.getUserId());  // userId from User object
            pstmt.setString(2, "Employee");  // Assuming all new users are employees, you can modify this based on your logic
            pstmt.setString(3, user.getPassword());  // password from User object
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignRole(int userId, String password) {
        String sql = "SELECT user_type FROM users WHERE user_id = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userType = rs.getString("user_type");
                if ("Administrator".equals(userType)) {
                    // Set role to admin_role (Administrator privileges)
                    try (PreparedStatement stmt = connection.prepareStatement("SET ROLE admin_role")) {
                        stmt.executeUpdate();
                    }
                } else if ("Employee".equals(userType)) {
                    // Set role to employee_role (Employee privileges)
                    try (PreparedStatement stmt = connection.prepareStatement("SET ROLE employee_role")) {
                        stmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

