import org.example.entity.MuseumArtifact;
import org.example.dao.MuseumArtifactDAO;
import org.example.service.MuseumArtifactService;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MuseumArtifactTest {
    private static Connection connection;
    private static MuseumArtifactDAO artifactDAO;
    //private EmployeeController employeeController;
    private static MuseumArtifactService artifactService;


    @BeforeAll
    public static void setupDatabase() throws SQLException {
        // Initialize a connection to the PostgreSQL database
        connection = DriverManager.getConnection("jdbc:postgresql://10.200.10.163:5444/museum", "postgres", "museum");
        artifactDAO = new MuseumArtifactDAO(connection);

    /*    // Create the MuseumArtifact table if it doesn't exist
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
        }*/
    }

    @BeforeEach
    public void setup() {
        // Initialize the artifactService before each test
        artifactService = new MuseumArtifactService(artifactDAO); // Ensure artifactService uses the artifactDAO
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


        artifactService.addArtifact(artifact);


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
        artifactService.addArtifact(artifact);


        MuseumArtifact retrievedArtifact = artifactService.getArtifact("Mona Lisa");

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

        artifactService.addArtifact(artifact);

        // test deleting the artifact
        boolean deleted = artifactService.deleteArtifact("Mona Lisa");
        assertTrue(deleted);

        // verify that the artifact no longer exists
        MuseumArtifact deletedArtifact = artifactService.getArtifact("Mona Lisa");
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

        artifactService.addArtifact(artifact);

        artifact.setDescription("A famous portrait by Leonardo da Vinci");
        boolean updated = artifactService.updateArtifact(artifact);
        assertTrue(updated);

        MuseumArtifact updatedArtifact = artifactService.getArtifact("Mona Lisa");
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

        artifactService.addArtifact(artifact1);
        artifactService.addArtifact(artifact2);
        artifactService.addArtifact(artifact3);

        // Test searching by category "Painting"
        List<MuseumArtifact> paintings = artifactService.searchArtifacts("Painting");

        // Verify that the list contains the correct number of artifacts and correct data
        assertEquals(2, paintings.size(), "There should be 2 paintings in the search results.");

        assertTrue(paintings.stream().anyMatch(artifact -> artifact.getName().equals("Mona Lisa")));
        assertTrue(paintings.stream().anyMatch(artifact -> artifact.getName().equals("The Starry Night")));

        // Test searching by category "Sculpture"
        List<MuseumArtifact> sculptures = artifactService.searchArtifacts("Sculpture");

        // Verify that the list contains the correct number of artifacts and correct data
        assertEquals(1, sculptures.size(), "There should be 1 sculpture in the search results.");
        assertTrue(sculptures.stream().anyMatch(artifact -> artifact.getName().equals("The Thinker")));

        // Test searching with a category that doesn't exist
        List<MuseumArtifact> nonExistingCategory = artifactService.searchArtifacts("Non-Existent Category");

        // Verify that no artifacts are returned
        assertTrue(nonExistingCategory.isEmpty(), "Search should return no results for a non-existent category.");
    }

    @Test
    public void testOrderByAscending() throws SQLException {
        MuseumArtifact artifact1 = new MuseumArtifact(
                "Mona Lisa",
                "Painting",
                "A portrait by Leonardo da Vinci",
                Date.valueOf("1503-01-01"),
                "Renaissance Section"
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
        //we temporarily add these to our database
        artifactDAO.addArtifact(artifact1);
        artifactDAO.addArtifact(artifact2);
        artifactDAO.addArtifact(artifact3);

        ArrayList<MuseumArtifact> orderedByName = artifactDAO.orderArtifactsByAscending("name");
        assertEquals(5, orderedByName.size(), "There should be 5 artifacts.");
        assertEquals("Mona Lisa", orderedByName.get(0).getName(), "First artifact should be 'Mona Lisa' (alphabetical order).");
        assertEquals("The Starry Night", orderedByName.get(1).getName(), "Second artifact should be 'The Starry Night'.");
        assertEquals("The Thinker", orderedByName.get(2).getName(), "Third artifact should be 'The Thinker'.");
    }
    @Test
    public void testOrderByDescending() throws SQLException {
            artifactDAO.addArtifact(new MuseumArtifact("Mona Lisa", "Painting", "A portrait by Leonardo da Vinci", Date.valueOf("1503-01-01"), "Renaissance Section"));
            artifactDAO.addArtifact(new MuseumArtifact("The Starry Night", "Painting", "A famous painting by Vincent van Gogh", Date.valueOf("1889-06-01"), "Museum of Modern Art"));
            artifactDAO.addArtifact(new MuseumArtifact("The Thinker", "Sculpture", "A famous sculpture by Auguste Rodin", Date.valueOf("1904-01-01"), "Rodin Museum"));

            ArrayList<MuseumArtifact> orderedByDate = artifactDAO.orderArtifactsByDescending("acquisition_date");

            assertEquals(3, orderedByDate.size(), "There should be 3 artifacts.");
            assertEquals("The Thinker", orderedByDate.get(0).getName(), "First artifact should be 'The Thinker' (latest acquisition date).");
            assertEquals("The Starry Night", orderedByDate.get(1).getName(), "Second artifact should be 'The Starry Night'.");
            assertEquals("Mona Lisa", orderedByDate.get(2).getName(), "Third artifact should be 'Mona Lisa' (oldest acquisition date).");
        }

    @Test
    public void testAcquisitionDateFiltering() throws SQLException {
    artifactDAO.addArtifact(new MuseumArtifact("Mona Lisa", "Painting", "A portrait by Leonardo da Vinci", Date.valueOf("1503-01-01"), "Renaissance Section"));
    artifactDAO.addArtifact(new MuseumArtifact("The Starry Night", "Painting", "A famous painting by Vincent van Gogh", Date.valueOf("1889-06-01"), "Museum of Modern Art"));
    artifactDAO.addArtifact(new MuseumArtifact("The Thinker", "Sculpture", "A famous sculpture by Auguste Rodin", Date.valueOf("1904-01-01"), "Rodin Museum"));
     ArrayList<MuseumArtifact> filteredArtifacts = artifactDAO.acquisitionDateFiltering("1800-01-01");
     assertEquals(2, filteredArtifacts.size(), "There should be 2 artifacts acquired after 1800.");
     assertTrue(filteredArtifacts.stream().anyMatch(a -> a.getName().equals("The Starry Night")), "The list should include 'The Starry Night'.");
     assertTrue(filteredArtifacts.stream().anyMatch(a -> a.getName().equals("The Thinker")), "The list should include 'The Thinker'.");
    }


    /*@Test
    void testAddObjectWithMissingFields() {
        // Test for missing fields (e.g., missing name)
        String name = "";  // Missing name
        String category = "Artifacts";
        String description = "A rare artifact.";
        String acquisitionDate = "2024-12-20";
        String locationInMuseum = "Section A";

        // Create a MuseumArtifact object using the constructor with missing name
        MuseumArtifact museumArtifact = new MuseumArtifact(
                name,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        // Expect IllegalArgumentException due to missing required fields
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.addArtifact(museumArtifact);
        });

        // Check that the exception message is correct
        assertEquals("All fields are required.", exception.getMessage(),
                "The exception message should indicate that all fields are required.");
    }*/

    @Test
    void testAddObjectWithMissingName() {
        // Test for missing name
        String name = "";  // Missing name
        String category = "Artifacts";
        String description = "A rare artifact.";
        String acquisitionDate = "2024-12-20";
        String locationInMuseum = "Section A";

        // Create a MuseumArtifact object using the constructor with missing name
        MuseumArtifact museumArtifact = new MuseumArtifact(
                name,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        // Expect IllegalArgumentException due to missing name
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.addArtifact(museumArtifact);
        });

        // Check that the exception message is correct
        assertEquals("Name is required.", exception.getMessage(),
                "The exception message should indicate that the name is required.");
    }

    @Test
    void testAddObjectWithMissingCategory() {
        // Test for missing category
        String name = "Artifact A";
        String category = "";  // Missing category
        String description = "A rare artifact.";
        String acquisitionDate = "2024-12-20";
        String locationInMuseum = "Section A";

        // Create a MuseumArtifact object using the constructor with missing category
        MuseumArtifact museumArtifact = new MuseumArtifact(
                name,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        // Expect IllegalArgumentException due to missing category
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.addArtifact(museumArtifact);
        });

        // Check that the exception message is correct
        assertEquals("Category is required.", exception.getMessage(),
                "The exception message should indicate that the category is required.");
    }

    @Test
    void testAddObjectWithMissingDescription() {
        // Test for missing description
        String name = "Artifact A";
        String category = "Artifacts";
        String description = "";  // Missing description
        String acquisitionDate = "2024-12-20";
        String locationInMuseum = "Section A";

        // Create a MuseumArtifact object using the constructor with missing description
        MuseumArtifact museumArtifact = new MuseumArtifact(
                name,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        // Expect IllegalArgumentException due to missing description
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.addArtifact(museumArtifact);
        });

        // Check that the exception message is correct
        assertEquals("Description is required.", exception.getMessage(),
                "The exception message should indicate that the description is required.");
    }

    @Test
    void testAddObjectWithMissingAcquisitionDate() {
        // Test for missing acquisition date
        String name = "Artifact A";
        String category = "Artifacts";
        String description = "A rare artifact.";
        String acquisitionDate = "";  // Missing acquisition date
        String locationInMuseum = "Section A";

        // Create a MuseumArtifact object using the constructor with missing acquisition date
        MuseumArtifact museumArtifact = new MuseumArtifact(
                name,
                category,
                description,
                null, // Set acquisitionDate as null to simulate missing date
                locationInMuseum
        );

        // Expect IllegalArgumentException due to missing acquisition date
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.addArtifact(museumArtifact);
        });

        // Check that the exception message is correct
        assertEquals("Acquisition Date is required.", exception.getMessage(),
                "The exception message should indicate that the acquisition date is required.");
    }

    @Test
    void testAddObjectWithMissingLocationInMuseum() {
        // Test for missing location in museum
        String name = "Artifact A";
        String category = "Artifacts";
        String description = "A rare artifact.";
        String acquisitionDate = "2024-12-20";
        String locationInMuseum = "";  // Missing location in museum

        // Create a MuseumArtifact object using the constructor with missing locationInMuseum
        MuseumArtifact museumArtifact = new MuseumArtifact(
                name,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        // Expect IllegalArgumentException due to missing locationInMuseum
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.addArtifact(museumArtifact);
        });

        // Check that the exception message is correct
        assertEquals("Location in Museum is required.", exception.getMessage(),
                "The exception message should indicate that the location in museum is required.");
    }


    @Test
    void testAddArtifactWithDuplicateName() {
        // Simulate the scenario where an artifact with the same name already exists in the database

        String name = "Artifact A";  // Name of the artifact
        String category = "Artifacts";
        String description = "A rare artifact.";
        String acquisitionDate = "2024-12-20";
        String locationInMuseum = "Section A";

        // Create a MuseumArtifact object
        MuseumArtifact artifactWithSameName = new MuseumArtifact(
                name,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );


        artifactService.addArtifact(artifactWithSameName);  // Add the artifact to simulate it already existing

        // Now try adding an artifact with the same name again
        MuseumArtifact duplicateArtifact = new MuseumArtifact(
                name,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );


        // Expect IllegalArgumentException due to duplicate artifact name
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.addArtifact(duplicateArtifact);
        });

        // Check that the exception message is correct
        assertEquals("An artifact with this name already exists.", exception.getMessage(),
                "The exception message should indicate that the artifact name must be unique.");
    }

    @Test
    void testUpdateArtifactWithDuplicateName() {
        // Step 1: Create the first artifact with name "Artifact X" (simulating an existing artifact in the database)
        String originalName = "Artifact X";  // Original name of the artifact
        String category = "Artifacts";
        String description = "A rare artifact.";
        String acquisitionDate = "2024-12-20";
        String locationInMuseum = "Section A";

        MuseumArtifact firstArtifact = new MuseumArtifact(
                originalName,
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        // Add the first artifact to the DAO (simulate it being in the system already)
        artifactService.addArtifact(firstArtifact);

        // Step 2: Create the second artifact with the new name "Artifact Y" (simulating the name conflict)
        String duplicateName = "Artifact Y";  // New name that already exists in the system
        MuseumArtifact secondArtifact = new MuseumArtifact(
                duplicateName,  // Attempting to update with a duplicate name
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        // Step 3: Simulate adding another artifact with name "Artifact Y"
        MuseumArtifact existingDuplicate = new MuseumArtifact(
                duplicateName,  // Existing artifact with the same name
                category,
                description,
                Date.valueOf(acquisitionDate),
                locationInMuseum
        );

        artifactService.addArtifact(existingDuplicate);  // Add it to simulate a conflict

        // Step 4: Try to update the first artifact to "Artifact Y" and expect an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artifactService.updateArtifact(secondArtifact);  // Attempting to update with duplicate name
        });

        // Step 5: Verify the error message
        assertEquals("An artifact with this name already exists.", exception.getMessage(),
                "The exception message should indicate that the artifact name must be unique during an update.");
    }



}
