import org.example.entity.MuseumArtifact;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.User;
import org.example.dao.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.*;

public class InitializationTest {

    private static Connection connection;
    private static UserDAO userDAO;
    private static MuseumArtifactDAO artifactDAO;

    @BeforeAll
    static void setupDatabaseConnection() throws SQLException {
        // Set up a database connection
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school", "postgres", "");
        userDAO = new UserDAO(connection);
        artifactDAO = new MuseumArtifactDAO(connection);
    }

    /*@AfterAll
    static void closeDatabaseConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }*/

    @BeforeEach
    void clearTables() throws SQLException {
        // Clear tables to ensure a clean state for each test
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM museum_artifacts");
            //stmt.executeUpdate("DELETE FROM employees");
            stmt.executeUpdate("DELETE FROM users");
        }
    }

    @Test
    void testInsertInitialData() throws SQLException {
        // Insert initial data into the users table
        User employeeUser = new User(2, "Employee", "employee1234");

        UserDAO userDAO = new UserDAO(connection);  // Assuming connection is already established
        userDAO.insertUser(employeeUser);
        MuseumArtifact artifact = new MuseumArtifact(
                "Mona Lisa",
                "Painting",
                "A famous painting by Leonardo da Vinci",
                Date.valueOf("1503-01-01"),
                "Louvre Museum");

        // Insert initial data into the employees table
        //userDAO.insertEmployee(1, "John Doe", "john.doe@example.com", "Curator", "Art Section", "Curator");
        //userDAO.insertEmployee(2, "Jane Smith", "jane.smith@example.com", "Guide", "History Section", "Guide");

        artifactDAO.addArtifact(artifact);
        // Insert initial data into the museum_artifacts table
        //artifactDAO.addArtifact("Mona Lisa", "Painting", "A famous painting by Leonardo da Vinci", "1503-01-01", "Louvre Museum");
        //artifactDAO.insertMuseumArtifact("The Thinker", "Sculpture", "A sculpture by Auguste Rodin", "1902-01-01", "Rodin Museum");

        // Verify users table

    }
}
