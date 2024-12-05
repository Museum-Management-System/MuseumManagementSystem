import org.example.entity.MuseumArtifact;
import org.example.dao.MuseumArtifactDAO;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MuseumDAOTest {
    private static Connection connection;
    private static MuseumArtifactDAO artifactDAO;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        // Initialize a connection to the PostgreSQL database
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Museum", "postgres", "museum");
        //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school", "postgres", "");
        artifactDAO = new MuseumArtifactDAO(connection);

        // Create the MuseumArtifact table if it doesn't exist
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS museum_artifacts (
                artifact_id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
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
    public void deleteAllArtifacts() throws SQLException {
        // Delete all records from the MuseumArtifact table before each test
        String deleteSQL = "DELETE FROM museum_artifacts";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteSQL);
        }
    }

    /*@AfterAll
    public static void tearDownDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }*/

    @Test
    public void testAddArtifact() throws SQLException {

        MuseumArtifact artifact = new MuseumArtifact(
                "Mona Lisa",
                "Painting",
                "A portrait by Leonardo da Vinci",
                Date.valueOf("1503-01-01"),
                "Louvre Museum"
        );


        artifactDAO.addArtifact(artifact);


        String querySQL = "SELECT * FROM museum_artifacts WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(querySQL)) {
            pstmt.setString(1, artifact.getName());
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next(), "Artifact should exist in the database");
            assertEquals("Mona Lisa", rs.getString("name"));
            assertEquals("Painting", rs.getString("category"));
            assertEquals("A portrait by Leonardo da Vinci", rs.getString("description"));
            assertEquals("1503-01-01", rs.getString("acquisition_date"));
            assertEquals("Louvre Museum", rs.getString("location"));
        }
    }

    @Test
    public void testGetArtifact() throws SQLException {

        MuseumArtifact artifact = new MuseumArtifact(
                "Mona Lisa",
                "Painting",
                "A portrait by Leonardo da Vinci",
                Date.valueOf("1503-01-01"),
                "Louvre Museum"
        );
        artifactDAO.addArtifact(artifact);


        MuseumArtifact retrievedArtifact = artifactDAO.getArtifact("Mona Lisa");

        // verify that the retrieved artifact matches the inserted one
        assertNotNull(retrievedArtifact, "Artifact should not be null");
        assertEquals("Mona Lisa", retrievedArtifact.getName());
        assertEquals("Painting", retrievedArtifact.getCategory());
        assertEquals("A portrait by Leonardo da Vinci", retrievedArtifact.getDescription());
        assertEquals(Date.valueOf("1503-01-01"), retrievedArtifact.getAcquisitionDate());
        assertEquals("Louvre Museum", retrievedArtifact.getLocationInMuseum());
    }

    @Test
    public void testDeleteArtifact() throws SQLException {

        MuseumArtifact artifact = new MuseumArtifact(
                "Mona Lisa",
                "Painting",
                "A portrait by Leonardo da Vinci",
                Date.valueOf("1503-01-01"),
                "Louvre Museum"
        );

        artifactDAO.addArtifact(artifact);

        // test deleting the artifact
        boolean deleted = artifactDAO.deleteArtifact("Mona Lisa");
        assertTrue(deleted);

        // verify that the artifact no longer exists
        MuseumArtifact deletedArtifact = artifactDAO.getArtifact("Mona Lisa");
        assertNull(deletedArtifact);
    }

    @Test
    public void testUpdateArtifact(){
        MuseumArtifact artifact = new MuseumArtifact(
                "Mona Lisa",
                "Painting",
                "A portrait by Leonardo da Vinci",
                Date.valueOf("1503-01-01"),
                "Louvre Museum"
        );

        artifactDAO.addArtifact(artifact);

        artifact.setDescription("A famous portrait by Leonardo da Vinci");
        boolean updated = artifactDAO.updateArtifact(artifact);
        assertTrue(updated);

        MuseumArtifact updatedArtifact = artifactDAO.getArtifact("Mona Lisa");
        assertNotNull(updatedArtifact);
        assertEquals("A famous portrait by Leonardo da Vinci", updatedArtifact.getDescription());
    }

    @Test
    public void testSearchArtifacts() throws SQLException {

        MuseumArtifact artifact1 = new MuseumArtifact(
                "Mona Lisa",
                "Painting",
                "A portrait by Leonardo da Vinci",
                Date.valueOf("1503-01-01"),
                "Louvre Museum"
        );
        MuseumArtifact artifact2 = new MuseumArtifact(
                "The Starry Night",
                "Painting",
                "A famous painting by Vincent van Gogh",
                Date.valueOf("1889-06-01"),
                "Museum of Modern Art"
        );
        MuseumArtifact artifact3 = new MuseumArtifact(
                "The Thinker",
                "Sculpture",
                "A famous sculpture by Auguste Rodin",
                Date.valueOf("1904-01-01"),
                "Rodin Museum"
        );

        artifactDAO.addArtifact(artifact1);
        artifactDAO.addArtifact(artifact2);
        artifactDAO.addArtifact(artifact3);

        // Test searching by category "Painting"
        List<MuseumArtifact> paintings = artifactDAO.searchArtifacts("Painting");

        // Verify that the list contains the correct number of artifacts and correct data
        assertEquals(2, paintings.size(), "There should be 2 paintings in the search results.");

        assertTrue(paintings.stream().anyMatch(artifact -> artifact.getName().equals("Mona Lisa")));
        assertTrue(paintings.stream().anyMatch(artifact -> artifact.getName().equals("The Starry Night")));

        // Test searching by category "Sculpture"
        List<MuseumArtifact> sculptures = artifactDAO.searchArtifacts("Sculpture");

        // Verify that the list contains the correct number of artifacts and correct data
        assertEquals(1, sculptures.size(), "There should be 1 sculpture in the search results.");
        assertTrue(sculptures.stream().anyMatch(artifact -> artifact.getName().equals("The Thinker")));

        // Test searching with a category that doesn't exist
        List<MuseumArtifact> nonExistingCategory = artifactDAO.searchArtifacts("Non-Existent Category");

        // Verify that no artifacts are returned
        assertTrue(nonExistingCategory.isEmpty(), "Search should return no results for a non-existent category.");
    }

}
