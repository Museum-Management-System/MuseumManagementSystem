package org.example.dao;

import org.example.entity.Administrator;

import java.sql.*;

public class AdministratorDAO {

    private int adminId;
    private String name;
    private String email;
    private String sectionName;

    public void Administrator(int adminId, String name, String email, String sectionName) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.sectionName = sectionName;
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:postgresql://10.200.10.163:5444/museum"; // Update DB URL
        String user = "postgres";
        String password = "museum";
        return DriverManager.getConnection(url, user, password);
    }

    public boolean addArtifact(String name, String category, String description, String acquisitionDate, String location) {
        String query = "INSERT INTO museum_artifacts (name, category, description, acquisition_date, location) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, description);
            pstmt.setDate(4, Date.valueOf(acquisitionDate)); // Format: YYYY-MM-DD
            pstmt.setString(5, location);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while adding artifact: " + e.getMessage());
            return false;
        }
    }

    public boolean updateArtifact(int artifactId, String name, String category, String description, String acquisitionDate, String location) {
        String query = "UPDATE museum_artifacts SET name = ?, category = ?, description = ?, acquisition_date = ?, location = ? WHERE artifact_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, description);
            pstmt.setDate(4, Date.valueOf(acquisitionDate));
            pstmt.setString(5, location);
            pstmt.setInt(6, artifactId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while updating artifact: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteArtifact(int artifactId) {
        String query = "DELETE FROM museum_artifacts WHERE artifact_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, artifactId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting artifact: " + e.getMessage());
            return false;
        }
    }

    public boolean addEmployee(String name, String email, String jobTitle, String sectionName, String role, String password) {
        String userInsertQuery = "INSERT INTO users (user_type, password) VALUES ('Employee', ?) RETURNING user_id";
        String employeeInsertQuery = "INSERT INTO employees (employee_id, name, email, job_title, section_name, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement userStmt = conn.prepareStatement(userInsertQuery);
             PreparedStatement empStmt = conn.prepareStatement(employeeInsertQuery)) {

            conn.setAutoCommit(false); // Start transaction

            userStmt.setString(1, password);
            ResultSet rs = userStmt.executeQuery();
            int userId;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            } else {
                conn.rollback();
                return false;
            }

            empStmt.setInt(1, userId);
            empStmt.setString(2, name);
            empStmt.setString(3, email);
            empStmt.setString(4, jobTitle);
            empStmt.setString(5, sectionName);
            empStmt.setString(6, role);

            empStmt.executeUpdate();
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error while adding employee: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(int employeeId, String name, String email, String jobTitle, String sectionName, String role) {
        String query = "UPDATE employees SET name = ?, email = ?, job_title = ?, section_name = ?, role = ? WHERE employee_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, jobTitle);
            pstmt.setString(4, sectionName);
            pstmt.setString(5, role);
            pstmt.setInt(6, employeeId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while updating employee: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM employees WHERE employee_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting employee: " + e.getMessage());
            return false;
        }
    }
}