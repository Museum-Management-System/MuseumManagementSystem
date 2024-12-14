import org.example.entity.User;
import org.example.dao.UserDAO;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private static Connection connection;
    private static UserDAO userDAO;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://10.200.10.163:5444/museum", "postgres", "museum");
        userDAO = new UserDAO(connection);
    }

    @BeforeEach
    public void clearUsersTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users");
        }
    }

    @Test
    public void testInsertUser() throws SQLException {
        User newUser = new User(1, "Employee", "password123");
        userDAO.insertUser(newUser);

        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newUser.getUserId());
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "User should be inserted into the database");
            assertEquals("Employee", rs.getString("user_type"), "User type should match");
            assertEquals("password123", rs.getString("password"), "Password should match");
        }
    }

    @Test
    public void testAuthenticateUserWithValidCredentials() {
        User newUser = new User(2, "Employee", "securePassword");
        userDAO.insertUser(newUser);

        String userType = userDAO.authenticateUser(newUser.getUserId(), newUser.getPassword());
        assertNotNull(userType, "User type should not be null for valid credentials");
        assertEquals("Employee", userType, "User type should match the inserted type");
    }

    @Test
    public void testAuthenticateUserWithInvalidCredentials() {
        User newUser = new User(3, "Employee", "securePassword");
        userDAO.insertUser(newUser);

        String userType = userDAO.authenticateUser(newUser.getUserId(), "wrongPassword");
        assertNull(userType, "Authentication should fail with invalid credentials");
    }

    @Test
    public void testAssignRoleAsAdministrator() throws SQLException {
        User adminUser = new User(4, "Administrator", "adminPassword");
        userDAO.insertUser(adminUser);

        userDAO.assignRole(adminUser.getUserId(), adminUser.getPassword());

        String sql = "SELECT current_user";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Role should be assigned and a query should execute");
            String currentRole = rs.getString(1);
            assertEquals("admin_role", currentRole, "Admin role should be set for Administrator users");
        }
    }

    @Test
    public void testAssignRoleAsEmployee() throws SQLException {
        User employeeUser = new User(5, "Employee", "employeePassword");
        userDAO.insertUser(employeeUser);

        userDAO.assignRole(employeeUser.getUserId(), employeeUser.getPassword());

        String sql = "SELECT current_user";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "Role should be assigned and a query should execute");
            String currentRole = rs.getString(1);
            assertEquals("employee_role", currentRole, "Employee role should be set for Employee users");
        }
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}