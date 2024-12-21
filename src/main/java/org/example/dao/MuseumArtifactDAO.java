package org.example.dao;

import org.example.entity.MuseumArtifact;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MuseumArtifactDAO {
    private Connection connection;
    public MuseumArtifactDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addArtifact(MuseumArtifact artifact) {
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
                return true; // Insertion was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Insertion failed
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
        String sql = "UPDATE museum_artifacts SET name = ?, category = ?, description = ?, acquisition_date = ?, location = ?" +
                " WHERE artifact_id = ?";

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

        // Start building the SQL query
        StringBuilder sql = new StringBuilder("SELECT * FROM museum_artifacts WHERE 1=1");

        // Add category filter if selected categories are not empty
        if (!categories.isEmpty()) {
            sql.append(" AND category IN (");
            for (int i = 0; i < categories.size(); i++) {
                sql.append("?");
                if (i < categories.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }
        if (minDate != null && !minDate.isEmpty() && maxDate != null && !maxDate.isEmpty()) {
            sql.append(" AND acquisition_date BETWEEN ? AND ?");
        }
        else if (minDate != null && !minDate.isEmpty()) {
            sql.append(" AND acquisition_date >= ?");
        }
        else if (maxDate != null && !maxDate.isEmpty()) {
            sql.append(" AND acquisition_date <= ?");
        }

        if (!locations.isEmpty()) {
            sql.append(" AND location IN (");
            for (int i = 0; i < locations.size(); i++) {
                sql.append("?");
                if (i < locations.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int index = 1;

            // Set the categories parameters in the prepared statement
            if (!categories.isEmpty()) {
                for (String category : categories) {
                    pstmt.setString(index++, category);
                }
            }

            if (minDate != null && !minDate.isEmpty() && maxDate != null && !maxDate.isEmpty()) {
                Date minSqlDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(minDate).getTime());
                Date maxSqlDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(maxDate).getTime());
                pstmt.setDate(index++, minSqlDate);
                pstmt.setDate(index++, maxSqlDate);
            }
            else if (minDate != null && !minDate.isEmpty()) {
                Date minSqlDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(minDate).getTime());
                pstmt.setDate(index++, minSqlDate);
            } else if (maxDate != null && !maxDate.isEmpty()) {
                Date maxSqlDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(maxDate).getTime());
                pstmt.setDate(index++, maxSqlDate);
            }

            if (!locations.isEmpty()) {
                for (String location : locations) {
                    pstmt.setString(index++, location);
                }
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MuseumArtifact artifact = new MuseumArtifact(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDate("acquisition_date"),
                        rs.getString("location")
                );
                artifacts.add(artifact);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return artifacts;
    }

}
