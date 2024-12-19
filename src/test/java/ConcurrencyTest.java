import org.example.entity.MuseumArtifact;
import org.example.dao.MuseumArtifactDAO;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrencyTest {
    private static Connection connection;
    private static MuseumArtifactDAO artifactDAO;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://10.200.10.163:5444/museum", "postgres", "museum");
        artifactDAO = new MuseumArtifactDAO(connection);

        // Ensure the table is created
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS museum_artifacts (
                artifact_id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL UNIQUE,
                category VARCHAR(100) NOT NULL,
                description TEXT,
                acquisition_date DATE,
                location VARCHAR(100)
            )
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @BeforeEach
    public void clearArtifactsTable() throws SQLException {
        String deleteSQL = "DELETE FROM museum_artifacts";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteSQL);
        }
    }

    @Test
    public void testConcurrentArtifactInsertion() throws InterruptedException {
        // Set up a thread pool for concurrency
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Define 10 unique artifacts to be inserted
        Runnable task = () -> {
            MuseumArtifact artifact = new MuseumArtifact(
                    "Artifact-" + Thread.currentThread().getId(),
                    "Category",
                    "Description",
                    Date.valueOf("2023-01-01"),
                    "Test Location"
            );
            artifactDAO.addArtifact(artifact);
        };

        // Execute tasks concurrently
        for (int i = 0; i < 10; i++) {
            executor.submit(task);
        }

        // Shut down the executor and await termination
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Executor tasks did not finish in time");

        // Verify that 10 unique artifacts were inserted
        String countSQL = "SELECT COUNT(*) FROM museum_artifacts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countSQL)) {
            assertTrue(rs.next(), "Count query should return a result");
            assertEquals(10, rs.getInt(1), "Exactly 10 artifacts should be in the database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}