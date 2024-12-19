package org.example.dao;

import org.example.entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private Connection connection;
    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }
    public boolean addArtifact(String name, String category, String description, String acquisitionDate, String location) {
        String query = "INSERT INTO museum_artifacts (name, category, description, acquisition_date, location) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setString(3, description);
            stmt.setDate(4, Date.valueOf(acquisitionDate)); // Format: YYYY-MM-DD
            stmt.setString(5, location);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while adding artifact: " + e.getMessage());
            return false;
        }
    }

    public boolean updateArtifact(int artifactId, String name, String category, String description, String acquisitionDate, String location) {
        String query = "UPDATE museum_artifacts SET name = ?, category = ?, description = ?, acquisition_date = ?, location = ? WHERE artifact_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setString(3, description);
            stmt.setDate(4, Date.valueOf(acquisitionDate));
            stmt.setString(5, location);
            stmt.setInt(6, artifactId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while updating artifact: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteArtifact(int artifactId) {
        String query = "DELETE FROM museum_artifacts WHERE artifact_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, artifactId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting artifact: " + e.getMessage());
            return false;
        }
    }
    //same with guestdao
    public ArrayList<String> searchObjects() {
        ArrayList<String> artifacts = new ArrayList<>();
        String query = "SELECT * FROM museum_artifacts";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artifacts.add(rs.getInt("artifact_id") + "," +rs.getString("name") + ", " +
                            rs.getString("category") + "," + rs.getString("location") + ", " +
                            rs.getDate("acquisition_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artifacts;
    }

    //same with guestdao
    public ArrayList<String> searchObjects(String name, String category, String location, Date acquisitionDate) {
        ArrayList<String> artifacts = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM museum_artifacts WHERE 'mms'='mms' ");
        if (name != null && !name.isEmpty()) {query.append(" AND name ILIKE ?");}
        if (category != null && !category.isEmpty()) {query.append(" AND category ILIKE ?");}
        if (location != null && !location.isEmpty()) {query.append(" AND location ILIKE ?");}
        if (acquisitionDate != null) {query.append(" AND acquisition_date = ?");}

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;
            if (name != null && !name.isEmpty()) {stmt.setString(index++, "%" + name + "%");}
            if (category != null && !category.isEmpty()) {stmt.setString(index++, "%" + category + "%");}
            if (location != null && !location.isEmpty()) {stmt.setString(index++, "%" + location + "%");}
            if (acquisitionDate != null) {stmt.setDate(index++, acquisitionDate);}

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artifacts.add(rs.getInt("artifact_id") + "," + rs.getString("name") + ", " +
                            rs.getString("category") + "," + rs.getString("location") + ", " +
                            rs.getDate("acquisition_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artifacts;
    }
}
