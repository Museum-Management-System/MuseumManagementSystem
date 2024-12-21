package org.example.service;

import org.example.dao.AdministratorDAO;
import org.example.dao.EmployeeDAO;
import org.example.entity.Employee;
import org.example.entity.MuseumArtifact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    private AdministratorDAO adminDAO;
    private Connection connection;

    public EmployeeService(AdministratorDAO adminDAO) {
        this.adminDAO = adminDAO;
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee findEmployee(int id) {
        String query = "SELECT * FROM employees WHERE employee_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone_num"),
                            resultSet.getString("job_title"),
                            resultSet.getString("section_name"),
                            resultSet.getString("role")
                    );
                } else {return null;}
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




    // Method to add a new employee
    public void addEmployee(Employee employee) {
        // Validate employee fields
        if (employee.getName() == null || employee.getName().isEmpty()
                || employee.getEmail() == null || employee.getEmail().isEmpty()
                || employee.getPhoneNum() == null || employee.getPhoneNum().isEmpty()
                || employee.getJobTitle() == null || employee.getJobTitle().isEmpty()
                || employee.getSectionName() == null || employee.getSectionName().isEmpty()
                || employee.getRole() == null || employee.getRole().isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }

        // Check if the employee already exists
        if (findEmployee(employee.getEmployeeId()) != null) {
            throw new IllegalArgumentException("An employee with this ID already exists.");
        }

        // Add the employee
        // Note: Add the actual saving functionality in the EmployeeDAO
        // employeeDAO.addEmployee(employee, connection);
    }

    // Method to get an employee by ID
    public Employee getEmployeeById(int employeeId) {
        Employee employee = findEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("No employee found with the given ID.");
        }
        return employee;
    }

    public boolean updateEmployee(Employee employee) {
        Employee existingEmployee = AdministratorDAO.getEmployee(employee.getName());
        if (existingEmployee != null && existingEmployee.getEmployeeId() != employee.getEmployeeId()) {
            throw new IllegalArgumentException("An employee with this name already exists.");
        }
        return AdministratorDAO.updateEmployee(employee);
    }

    // Method to delete an employee
    public boolean deleteEmployee(int employeeId) {
        Employee existingEmployee = findEmployee(employeeId);
        if (existingEmployee == null) {
            throw new IllegalArgumentException("No employee found with the given ID.");
        }

        // Implement the delete functionality in the DAO
        return adminDAO.deleteEmployee(employeeId);
    }

    // Method to get all employees
    public List<Employee> getAllEmployees() {
        // Implement the retrieval logic in the EmployeeDAO
        // List<Employee> employees = employeeDAO.getAllEmployees(connection);
        return null; // Replace this with actual retrieval from the DAO
    }

    // Method to search employees by role
    public List<Employee> searchEmployeesByRole(String role) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty.");
        }

        // Implement the search logic in EmployeeDAO
        // List<Employee> employees = employeeDAO.searchEmployeesByRole(role, connection);
        return null; // Replace this with actual search functionality
    }
}