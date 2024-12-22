import org.example.dao.AdministratorDAO;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.Employee;
import org.example.entity.MuseumArtifact;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AdministratorDAOTest {
    private static Connection connection;
    private static AdministratorDAO administratorDAO;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        // Initialize a connection to the PostgreSQL database
        connection = DriverManager.getConnection("jdbc:postgresql://10.200.10.163:5444/museum", "postgres", "museum");
        administratorDAO = new AdministratorDAO(connection);

        // Check if the table already exists
        String checkTableExistsSQL = "SELECT to_regclass('public.employees')";
        try (Statement checkStmt = connection.createStatement();
             ResultSet rs = checkStmt.executeQuery(checkTableExistsSQL)) {

            if (rs.next() && rs.getString(1) == null) {
                // Create the MuseumArtifact table if it doesn't exist
                String createTableSQL = """
                CREATE TABLE employees (
                employee_id INT PRIMARY KEY NOT NULL UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                phone_num VARCHAR(20) NOT NULL UNIQUE,
                job_title VARCHAR(100),
                section_name VARCHAR(100),
                image BYTEA
                );
            """;
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createTableSQL);
                }
            }
        }
    }
    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testAddEmployee() throws SQLException {
        Employee employee = new Employee(
                100,
                "Suna Ari",
                "sunaari@gmail.com",
                "History Guide",
                "+12345678",
                "Guides",
                null
        );

        administratorDAO.addEmployee(employee, "password123");


        String querySQL = "SELECT * FROM employees WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, employee.getEmail());
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Employee should exist in the database");
            assertEquals("Suna Ari", rs.getString("name"));
            assertEquals("sunaari@gmail.com", rs.getString("email"));
            assertEquals("History Guide", rs.getString("job_title"));
            assertEquals("+12345678", rs.getString("phone_num"));
            assertEquals("Guides", rs.getString("section_name"));
            assertEquals(null, rs.getBytes("image"));
            administratorDAO.deleteEmployee(rs.getInt("employee_id"));
        }
    }

    @Test
    public void testGetEmployee() throws SQLException {
        Employee employee = new Employee(
                100,
                "Sinem Ari",
                "sinem@gmail.com",
                "History Guide",
                "+623456",
                "Guides",
                null
        );

        administratorDAO.addEmployee(employee, "password123");


        ArrayList<Employee> retrievedEmployees = administratorDAO.searchEmployees("Sinem Ari");

        String querySQL = "SELECT * FROM employees WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, employee.getName());
            ResultSet rs = pstmt.executeQuery();

            assertFalse(retrievedEmployees.isEmpty(), "At least one employee should be found with the given name");

            boolean employeeFound = false;
            for(Employee emp : retrievedEmployees) {
                if(emp.getEmail().equals("sinem@gmail.com")) { //emails are unique
                    employeeFound = true;
                    assertEquals("Sinem Ari", emp.getName());
                    assertEquals("History Guide", emp.getJobTitle());
                    assertEquals("+623456", emp.getPhoneNum());
                    assertEquals("Guides", emp.getSectionName());
                    assertNull(emp.getImageData());
                    administratorDAO.deleteEmployee(employee.getEmployeeId());
                    break;
                }
            }
        }
    }
    @Test
    public void testEmployeeIdsAreUnique() throws SQLException {
        // Define sample employees
        Employee employee1 = new Employee(
                0, //dummy value
                "Suna Ari",
                "sunaari@gmail.com",
                "History Guide",
                "+123456",
                "Guides",
                null
        );

        Employee employee2 = new Employee(
                0,
                "John Doe",
                "johndoe@gmail.com",
                "Art Curator",
                "+654321",
                "Curators",
                null
        );

        Employee employee3 = new Employee(
                0,
                "Jane Smith",
                "janesmith@gmail.com",
                "Exhibit Designer",
                "+789012",
                "Designers",
                null
        );

        // Add employees to the database
        boolean addEmployee1 = administratorDAO.addEmployee(employee1, "password123");
        boolean addEmployee2 = administratorDAO.addEmployee(employee2, "password234");
        boolean addEmployee3 = administratorDAO.addEmployee(employee3, "password345");

        // Check if employees are added successfully
        assertTrue(addEmployee1, "Employee1 should be added successfully");
        assertTrue(addEmployee2, "Employee2 should be added successfully");
        assertTrue(addEmployee3, "Employee3 should be added successfully");

        // Query the database for all employee IDs
        String querySQL = "SELECT employee_id FROM employees";
        Set<Integer> employeeIds = new HashSet<>();

        try (PreparedStatement pstmt = connection.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                assertTrue(employeeIds.add(employeeId), "Employee ID should be unique");
                administratorDAO.deleteEmployee(rs.getInt("employee_id"));
            }
        }
    }
    @Test public void testMissingRequiredFields() throws SQLException {
        Employee employee = new Employee(
                0,
                null, // Missing name
                "sunaari@gmail.com",
                "History Guide",
                "+123456",
                "Guides",
                null
        );
        boolean result = administratorDAO.addEmployee(employee, "password123");
        assertFalse(result, "Employee with missing required fields should not be added");
    }
    @Test
    public void testUpdateEmployee() throws SQLException {
        // Step 1: Set Up Initial Employee
        Employee initialEmployee = new Employee(
                104,
                "Jane Doe",
                "janedoe@example.com",
                "History Guide",
                "+9876543210",
                "Guides",
                null
        );

        boolean addResult = administratorDAO.addEmployee(initialEmployee, "password123");
        assertTrue(addResult, "Initial employee should be added successfully");

        String query = "SELECT employee_id FROM employees WHERE email = ?";
        int emp_id = 0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, initialEmployee.getEmail()); //email is unique
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Added employee should be found in the database");
            emp_id = rs.getInt("employee_id");
            initialEmployee.setEmployeeId(emp_id);
        }

        // Step 2: Update Employee Details
        initialEmployee.setName("Jane Smith");
        initialEmployee.setEmail("janesmith@example.com");
        initialEmployee.setPhoneNum("+1112223333");
        initialEmployee.setJobTitle("Senior Guide");
        initialEmployee.setSectionName("Exhibits");
        initialEmployee.setImageData(null); // Assuming no image data for simplicity

        // Step 3: Invoke Update Method
        boolean updateResult = administratorDAO.updateEmployee(initialEmployee);
        assertTrue(updateResult, "Employee should be updated successfully");

        // Step 4: Verify Update
        String querySQL = "SELECT * FROM employees WHERE employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setInt(1, emp_id);
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Updated employee should be found in the database");
            assertEquals("Jane Smith", rs.getString("name"));
            assertEquals("janesmith@example.com", rs.getString("email"));
            assertEquals("+1112223333", rs.getString("phone_num"));
            assertEquals("Senior Guide", rs.getString("job_title"));
            assertEquals("Exhibits", rs.getString("section_name"));
            assertNull(rs.getBytes("image"));
            administratorDAO.deleteEmployee(rs.getInt("employee_id"));
        }
    }
    @Test
    public void testDeleteEmployee() throws SQLException {
        // Step 1: Add the employee to the database
        Employee employee = new Employee(
                100,
                "Suna",
                "suna@gmail.com",
                "History Guide",
                "+123456",
                "Guides",
                null
        );

        boolean addResult = administratorDAO.addEmployee(employee, "password123");
        assertTrue(addResult, "Employee should be added successfully");

        // Step 2: Retrieve the employee ID assigned by the database
        String queryIdSQL = "SELECT employee_id FROM employees WHERE email = ?";
        int assignedId = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(queryIdSQL)) {
            pstmt.setString(1, employee.getEmail());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                assignedId = rs.getInt("employee_id");
            }
        }
        assertTrue(assignedId > 0, "Assigned employee ID should be greater than 0");

        // Step 3: Delete the employee using the assigned ID
        boolean deleteResult = administratorDAO.deleteEmployee(assignedId);
        assertTrue(deleteResult, "Employee should be deleted successfully");

        // Step 4: Verify the employee has been deleted
        String verifySQL = "SELECT * FROM employees WHERE employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(verifySQL)) {
            pstmt.setInt(1, assignedId);
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next(), "Employee record should be permanently deleted from the database");
        }
    }
}
