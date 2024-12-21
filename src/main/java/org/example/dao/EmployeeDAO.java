package org.example.dao;

import org.example.entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private Connection connection;
    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }
}
