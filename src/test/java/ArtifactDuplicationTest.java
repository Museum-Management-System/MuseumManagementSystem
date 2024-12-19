import org.example.entity.MuseumArtifact;
import org.example.dao.MuseumArtifactDAO;
import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ArtifactDuplicationTest {
    private static Connection connection;
    private static MuseumArtifactDAO artifactDAO;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://10.200.10.163:5444/museum", "postgres", "museum");
        artifactDAO = new MuseumArtifactDAO(connection);
    }

    @BeforeEach
    public void clearArtifactsTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM museum_artifacts");
        }
    }

    @Test
    public void testPreventDuplicateArtifactInsertion() throws SQLException {
        // Insert an initial artifact
        MuseumArtifact artifact = new MuseumArtifact(
                "Venus de Milo",
                "Sculpture",
                "An ancient Greek sculpture",
                Date.valueOf("100-01-01"),
                "Louvre Museum"
        );
        artifactDAO.addArtifact(artifact);

        // Attempt to insert the same artifact again
        Exception exception = assertThrows(SQLException.class, () -> artifactDAO.addArtifact(artifact));

        // Verify the exception message contains a duplicate key error
        assertTrue(exception.getMessage().contains("duplicate key"), "Should prevent duplicate artifact insertion");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}