import org.example.entity.MuseumArtifact;
import org.example.entity.User;
import org.example.dao.MuseumArtifactDAO;
import org.example.dao.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {
    private static Connection connection;
    private static MuseumArtifactDAO artifactDAO;
    private static UserDAO userDAO;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://10.200.10.163:5444/museum", "postgres", "museum");
        artifactDAO = new MuseumArtifactDAO(connection);
        userDAO = new UserDAO(connection);
    }

    @BeforeEach
    public void clearTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM museum_artifacts");
            stmt.executeUpdate("DELETE FROM users");
        }
    }

    @Test
    public void testArtifactValidation() {
        MuseumArtifact invalidArtifact = new MuseumArtifact(
                null,  // Null name
                "Painting",
                "A painting with invalid data",
                Date.valueOf("1503-01-01"),
                "Louvre Museum",
                null
        );

        Exception exception = assertThrows(SQLException.class, () -> artifactDAO.addArtifact(invalidArtifact));
        assertTrue(exception.getMessage().contains("null value"), "Exception should mention null value for name");
    }

    @Test
    public void testUserValidation() {
        User invalidUser = new User(
                1,
                null,  // Null username
                "password123"
        );

        Exception exception = assertThrows(SQLException.class, () -> userDAO.insertUser(invalidUser));
        assertTrue(exception.getMessage().contains("null value"), "Exception should mention null value for username");
    }

    @Test
    public void testArtifactFieldLengthValidation() {
        MuseumArtifact invalidArtifact = new MuseumArtifact(
                "A".repeat(101),  // Name exceeding 100 characters
                "Painting",
                "Description",
                Date.valueOf("1503-01-01"),
                "Louvre Museum",
                null
        );

        Exception exception = assertThrows(SQLException.class, () -> artifactDAO.addArtifact(invalidArtifact));
        assertTrue(exception.getMessage().contains("value too long"), "Exception should mention field length exceeded");
    }

    @Test
    public void testUserFieldLengthValidation() {
        User invalidUser = new User(
                1,
                "A".repeat(101),  // Username exceeding 100 characters
                "password123"
        );

        Exception exception = assertThrows(SQLException.class, () -> userDAO.insertUser(invalidUser));
        assertTrue(exception.getMessage().contains("value too long"), "Exception should mention field length exceeded");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
