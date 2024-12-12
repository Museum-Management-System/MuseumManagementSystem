package org.example.dao;

import org.example.entity.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO {

    public static Employee findEmployee(int id, Connection connection) {
        String query = "SELECT * FROM employees WHERE employee_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
