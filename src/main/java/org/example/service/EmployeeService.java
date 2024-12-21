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
                            resultSet.getBytes("image")
                    );
                } else {return null;}
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
}