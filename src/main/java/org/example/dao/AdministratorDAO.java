package org.example.dao;

import org.example.entity.Administrator;
import org.example.entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministratorDAO {
    private Connection connection;
    public AdministratorDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addEmployee(String name, String email, String jobTitle, String sectionName, String role, String password) {
        String userQuery = "INSERT INTO users (user_type, password) VALUES ('Employee', ?) RETURNING user_id;";
        String employeeQuery = "INSERT INTO employees (employee_id, name, email, job_title, section_name, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement userStmt = connection.prepareStatement(userQuery);
             PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery)) {
            connection.setAutoCommit(false);
            userStmt.setString(1, password);
            ResultSet rs = userStmt.executeQuery();
            int userId;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            } else {
                connection.rollback();
                return false;
            }
            employeeStmt.setInt(1, userId);
            employeeStmt.setString(2, name);
            employeeStmt.setString(3, email);
            employeeStmt.setString(4, jobTitle);
            employeeStmt.setString(5, sectionName);
            employeeStmt.setString(6, role);

            int rowsAffected = employeeStmt.executeUpdate();
            if (rowsAffected > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                System.err.println("Rolling back transaction failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error while adding employee: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto commit: " + e.getMessage());
            }
        }
    }
    //parameterless search for employees
    public ArrayList<Employee> searchEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(new Employee(rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("job_title"),
                            rs.getString("phone_num"),
                            rs.getString("section_name"),
                            rs.getString("role")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    //and parametered search for employees (same logic with artifacts)
    public ArrayList<String> searchEmployees(String name, String email, String role, String jobTitle, String sectionName) {
        ArrayList<String> employees = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM employees WHERE 1=1");
        if (name != null && !name.isEmpty()) {query.append(" AND name ILIKE ?");}
        if (email != null && !email.isEmpty()) {query.append(" AND email ILIKE ?");}
        if (role != null && !role.isEmpty()) {query.append(" AND role = ?");}
        if (jobTitle != null && !jobTitle.isEmpty()) {query.append(" AND job_title ILIKE ?");}
        if (sectionName != null && !sectionName.isEmpty()) {query.append(" AND section_name ILIKE ?");}

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;
            if (name != null && !name.isEmpty()) {stmt.setString(index++, "%" + name + "%");}
            if (email != null && !email.isEmpty()) {stmt.setString(index++, "%" + email + "%");}
            if (role != null && !role.isEmpty()) {stmt.setString(index++, role);}
            if (jobTitle != null && !jobTitle.isEmpty()) {stmt.setString(index++, "%" + jobTitle + "%");}
            if (sectionName != null && !sectionName.isEmpty()) {stmt.setString(index++, "%" + sectionName + "%");}
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(rs.getInt("employee_id") + "," +
                            rs.getString("name") + "," + rs.getString("email") + "," +
                            rs.getString("phone_num") + "," + rs.getString("job_title") + "," +
                            rs.getString("section_name") + "," + rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }


    public boolean updateEmployee(int employeeId, String name, String email, String jobTitle, String sectionName, String role) {
        String query = "UPDATE employees SET name = ?, email = ?, job_title = ?, section_name = ?, role = ? WHERE employee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, jobTitle);
            stmt.setString(4, sectionName);
            stmt.setString(5, role);
            stmt.setInt(6, employeeId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while updating employee: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM employees WHERE employee_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting employee: " + e.getMessage());
            return false;
        }
    }

    //same with employeedao
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
    //same with employeedao
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
    //same with employeedao
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