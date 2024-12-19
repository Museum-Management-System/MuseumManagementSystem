package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestUserDAO {
    private Connection connection;
    public GuestUserDAO(Connection connection) {
        this.connection = connection;
    }

    //this is the parameterless search
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

    //and here is the parametered one
    public ArrayList<String> searchObjects(String name, String category, String location, Date acquisitionDate) {
        ArrayList<String> artifacts = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM museum_artifacts WHERE 'mms'='mms' ");
        //a statement which is always true, will be concatenated with and statements.
        if (name != null && !name.isEmpty()) {query.append(" AND name ILIKE ?");}
        if (category != null && !category.isEmpty()) {query.append(" AND category ILIKE ?");}
        if (location != null && !location.isEmpty()) {query.append(" AND location ILIKE ?");}
        if (acquisitionDate != null) {query.append(" AND acquisition_date = ?");}

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;
            //i allowed partial matching, "alike"ness.
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
