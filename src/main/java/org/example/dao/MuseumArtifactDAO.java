package org.example.dao;

import org.example.entity.MuseumArtifact;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                MuseumArtifact artifact = new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location"));
                artifact.setArtifactId(rs.getInt("artifact_id"));
                return artifact;
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
        String sql = "UPDATE museum_artifacts SET name = ?, category = ?, description = ?, acquisition_date = ?, location = ? WHERE artifact_id = ?";
        // Assuming ps is your PreparedStatement and you need to set the acquisitionDate


        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, artifact.getName());
            pstmt.setString(2, artifact.getCategory());
            pstmt.setString(3, artifact.getDescription());
            //pstmt.setDate(3, (Date) artifact.getAcquisitionDate());
            pstmt.setDate(4, new java.sql.Date(artifact.getAcquisitionDate().getTime()));
            pstmt.setString(5, artifact.getLocationInMuseum());
            pstmt.setInt(6, artifact.getArtifactId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Returns true if the artifact was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Returns false if update failed
    }
    public ArrayList<MuseumArtifact> searchArtifacts(String name) {
        ArrayList<MuseumArtifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM museum_artifacts WHERE LOWER(name) LIKE LOWER(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,"%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MuseumArtifact artifact = new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location"));
                artifact.setArtifactId(rs.getInt("artifact_id"));
                artifacts.add(artifact);
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
                MuseumArtifact artifact = new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location"));
                artifact.setArtifactId(rs.getInt("artifact_id"));
                artifacts.add(artifact);
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
    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM museum_artifacts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public ArrayList<String> getAllLocations() {
        ArrayList<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT location FROM museum_artifacts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public ArrayList<MuseumArtifact> filterArtifacts(ArrayList<String> categories, String minDate, String maxDate, ArrayList<String> locations) {
        ArrayList<MuseumArtifact> artifacts = new ArrayList<>();
        String sql = "SELECT * FROM museum_artifacts WHERE "
                + "(category IN (?) OR ? IS NULL) AND "
                + "(acquisition_date BETWEEN ? AND ? OR (? IS NULL AND ? IS NULL)) AND "
                + "(location IN (?) OR ? IS NULL)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            Date minSqlDate = null;
            Date maxSqlDate = null;
            if (minDate != null && !minDate.isEmpty()) {minSqlDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(minDate).getTime());}
            if (maxDate != null && !maxDate.isEmpty()) {maxSqlDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(maxDate).getTime());}
            pstmt.setString(1, String.join(",", categories));
            pstmt.setString(2, categories.isEmpty() ? null : "dummy");
            pstmt.setDate(3, minSqlDate);
            pstmt.setDate(4, maxSqlDate);
            pstmt.setDate(5, minSqlDate);
            pstmt.setDate(6, maxSqlDate);
            pstmt.setString(7, String.join(",", locations));
            pstmt.setString(8, locations.isEmpty() ? null : "dummy");

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
        } catch (SQLException  | ParseException e) {
            e.printStackTrace();
        }
        return artifacts;
    }
}
