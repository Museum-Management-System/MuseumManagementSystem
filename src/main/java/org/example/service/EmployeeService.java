package org.example.service;

import org.example.dao.EmployeeDAO;
import org.example.entity.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    private EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    private Connection connection;
    public EmployeeService(Connection connection) {
        this.connection = connection;
    }

    public static Employee findEmployee(int id, Connection connection) {
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
    public void addEmployee(Employee employee, Connection connection) {
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
        if (findEmployee(employee.getEmployeeId(), connection) != null) {
            throw new IllegalArgumentException("An employee with this ID already exists.");
        }

        // Add the employee
        // Note: Add the actual saving functionality in the EmployeeDAO
        // employeeDAO.addEmployee(employee, connection);
    }

    // Method to get an employee by ID
    public Employee getEmployeeById(int employeeId, Connection connection) {
        Employee employee = findEmployee(employeeId, connection);
        if (employee == null) {
            throw new IllegalArgumentException("No employee found with the given ID.");
        }
        return employee;
    }

    // Method to update an employee
    public void updateEmployee(Employee employee, Connection connection) {
        Employee existingEmployee = findEmployee(employee.getEmployeeId(), connection);
        if (existingEmployee == null) {
            throw new IllegalArgumentException("No employee found with the given ID.");
        }

        // Implement the update functionality in the DAO
        // employeeDAO.updateEmployee(employee, connection);
    }

    // Method to delete an employee
    public void deleteEmployee(int employeeId, Connection connection) {
        Employee existingEmployee = findEmployee(employeeId, connection);
        if (existingEmployee == null) {
            throw new IllegalArgumentException("No employee found with the given ID.");
        }

        // Implement the delete functionality in the DAO
        // employeeDAO.deleteEmployee(employeeId, connection);
    }

    // Method to get all employees
    public List<Employee> getAllEmployees(Connection connection) {
        // Implement the retrieval logic in the EmployeeDAO
        // List<Employee> employees = employeeDAO.getAllEmployees(connection);
        return null; // Replace this with actual retrieval from the DAO
    }

    // Method to search employees by role
    public List<Employee> searchEmployeesByRole(String role, Connection connection) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty.");
        }

        // Implement the search logic in EmployeeDAO
        // List<Employee> employees = employeeDAO.searchEmployeesByRole(role, connection);
        return null; // Replace this with actual search functionality
    }
}