package org.example.dao;

import org.example.entity.MuseumArtifact;

import java.sql.*;
import java.util.ArrayList;

public class MuseumArtifactDAO {
    private Connection connection;
    public MuseumArtifactDAO(Connection connection) {
        this.connection = connection;
    }

    public void addArtifact(MuseumArtifact artifact) {
        String sql = "INSERT INTO museum_artifacts (name, category, description, acquisition_date, location) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING artifact_id";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, artifact.getName());
            pstmt.setString(2, artifact.getCategory());
            pstmt.setString(3, artifact.getDescription());
            pstmt.setDate(4, new java.sql.Date(artifact.getAcquisitionDate().getTime()));  // Use correct SQL date type
            pstmt.setString(5, artifact.getLocationInMuseum());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt("artifact_id");
                artifact.setArtifactId(generatedId); // Set the generated artifact_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding artifact: " + e.getMessage());
        }
    }
    public MuseumArtifact getArtifact(String artifactName) {
        String sql = "SELECT * FROM museum_artifacts WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, artifactName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteArtifact(String artifactName) {
        String sql = "DELETE FROM museum_artifacts WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, artifactName);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Returns true if artifact was deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Returns false if deletion failed
    }

    public boolean updateArtifact(MuseumArtifact artifact) {
        String sql = "UPDATE museum_artifacts SET category = ?, description = ?, acquisition_date = ?, location = ? WHERE name = ?";
        // Assuming ps is your PreparedStatement and you need to set the acquisitionDate


        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, artifact.getCategory());
            pstmt.setString(2, artifact.getDescription());
            //pstmt.setDate(3, (Date) artifact.getAcquisitionDate());
            pstmt.setDate(3, new java.sql.Date(artifact.getAcquisitionDate().getTime()));
            pstmt.setString(4, artifact.getLocationInMuseum());
            pstmt.setString(5, artifact.getName());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Returns true if the artifact was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Returns false if update failed
    }
    public ArrayList<MuseumArtifact> searchArtifacts(String category) {
        ArrayList<MuseumArtifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM museum_artifacts WHERE category = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                artifacts.add(new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artifacts;
    }

    public ArrayList<MuseumArtifact> getAllArtifacts() {
        ArrayList<MuseumArtifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM museum_artifacts";  // No WHERE clause, to fetch all artifacts
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                artifacts.add(new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artifacts;
    }


    //Object name,acquisition date, object type ve acquisition date göre filtreleme
    public ArrayList<MuseumArtifact> orderArtifactsByAscending(String attributeName) {
        ArrayList<MuseumArtifact> artifacts = new ArrayList<>();
        String query="SELECT * FROM museum_artifacts order by ? asc;";
        try(PreparedStatement preparedStatement= connection.prepareStatement(query)){
            preparedStatement.setString(1, attributeName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                artifacts.add(new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artifacts;
    }
    //This is used for Z->A, newest date -> oldest and a->z
    public ArrayList<MuseumArtifact> orderArtifactsByDescending(String attributeName) {
        ArrayList<MuseumArtifact> artifacts = new ArrayList<>();
        String query=" SELECT * FROM museum_artifacts order by ? desc ;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, attributeName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                artifacts.add(new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location")
                ));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return artifacts;
    }
    //Filter from an old date to current date.
    public ArrayList<MuseumArtifact> acquisitionDateFiltering(String acquisitionDate) {
        ArrayList<MuseumArtifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM museum_artifacts WHERE acquisition_date > ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, acquisitionDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                artifacts.add(new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artifacts;
    }
}