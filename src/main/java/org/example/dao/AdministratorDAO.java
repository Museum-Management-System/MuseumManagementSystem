package org.example.dao;

import org.example.entity.Employee;

import java.sql.*;
import java.util.ArrayList;

public class AdministratorDAO {
    private static Connection connection;
    public AdministratorDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addEmployee(String name, String email, String jobTitle, String sectionName, String password, byte[] imageData) {
        String userQuery = "INSERT INTO users (user_type, password) VALUES ('Employee', ?) RETURNING user_id;";
        String employeeQuery = "INSERT INTO employees (employee_id, name, email, job_title, section_name, image) VALUES (?, ?, ?, ?, ?, ?)";

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
            employeeStmt.setBytes(6, imageData);

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
                            rs.getBytes("image")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    //and parametered search for employees (same logic with artifacts)
    public ArrayList<Employee> searchEmployees(String name) {
        ArrayList<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees WHERE LOWER (name) LIKE LOWER(?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(new Employee(rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("job_title"),
                            rs.getString("phone_num"),
                            rs.getString("section_name"),
                            rs.getBytes("image")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    public static boolean updateEmployee(Employee employee) {
        String query = "UPDATE employees SET name = ?, email = ?, phone_num = ?, job_title = ?, section_name = ?, image = ?" +
                " WHERE employee_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPhoneNum());
            stmt.setString(4, employee.getJobTitle());
            stmt.setString(5, employee.getSectionName());
            stmt.setBytes(6, employee.getImageData());
            stmt.setInt(7, employee.getEmployeeId());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static Employee getEmployee(String name) {
        String query = "SELECT * FROM employees WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_num"),
                        rs.getString("job_title"),
                        rs.getString("section_name"),
                        rs.getBytes("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
    public ArrayList<Employee> filterEmployees(ArrayList<String> selectedJobTitles, ArrayList<String> selectedSections) {
        ArrayList<Employee> filteredEmployees = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM employees WHERE 1=1");

        if (!selectedJobTitles.isEmpty()) {
            query.append(" AND job_title IN (");
            for (int i = 0; i < selectedJobTitles.size(); i++) {
                query.append("?");
                if (i < selectedJobTitles.size() - 1) query.append(",");
            }
            query.append(")");
        }

        if (!selectedSections.isEmpty()) {
            query.append(" AND section_name IN (");
            for (int i = 0; i < selectedSections.size(); i++) {
                query.append("?");
                if (i < selectedSections.size() - 1) query.append(",");
            }
            query.append(")");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;

            // Set job titles parameters
            if (!selectedJobTitles.isEmpty()) {
                for (String jobTitle : selectedJobTitles) {
                    stmt.setString(index++, jobTitle);
                }
            }

            // Set sections parameters
            if (!selectedSections.isEmpty()) {
                for (String section : selectedSections) {
                    stmt.setString(index++, section);
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_num"),
                        rs.getString("job_title"),
                        rs.getString("section_name"),
                        rs.getBytes("image")
                );
                filteredEmployees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredEmployees;
    }

    public ArrayList<String> getAllJobTitles() {
        ArrayList<String> jobTitles = new ArrayList<>();
        String query = "SELECT DISTINCT job_title FROM employees";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                jobTitles.add(rs.getString("job_title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobTitles;
    }

    public ArrayList<String> getAllSections() {
        ArrayList<String> sections = new ArrayList<>();
        String query = "SELECT DISTINCT section_name FROM employees";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                sections.add(rs.getString("section_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sections;
    }
}
