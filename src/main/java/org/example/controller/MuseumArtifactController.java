package org.example.controller;

import org.example.entity.MuseumArtifact;
import org.example.service.MuseumArtifactService;
import org.example.view.MuseumArtifactView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MuseumArtifactController {
    private MuseumArtifactView artifactView;
    private MuseumArtifactService artifactService;

    public MuseumArtifactController(MuseumArtifactView artifactView, MuseumArtifactService artifactService) {
        this.artifactView = artifactView;
        this.artifactService = artifactService;

        this.artifactView.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddArtifact();
            }
        });

        this.artifactView.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchArtifact();
            }
        });

        artifactView.getSearchByCategoryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchByCategory();
            }
        });

    }
    private void handleSearchByCategory() {
        try {
            String category = artifactView.getSearchCategoryFieldInput();
            if (category.isEmpty()) {
                artifactView.setMessage("Please enter a category.");
                return;
            }

            // Retrieve artifacts from the service
            List<MuseumArtifact> artifacts = artifactService.searchArtifacts(category);

            if (!artifacts.isEmpty()) {
                // Transform the list of MuseumArtifact into table-compatible data
                List<String[]> tableData = new ArrayList<>();
                for (MuseumArtifact artifact : artifacts) {
                    tableData.add(new String[]{
                            artifact.getName(),
                            artifact.getCategory(),
                            artifact.getDescription(),
                            artifact.getAcquisitionDate().toString(),
                            artifact.getLocationInMuseum()
                    });
                }

                // Update the table in the view
                artifactView.updateCategoryTable(tableData);
                artifactView.setMessage("Artifacts retrieved successfully.");
            } else {
                artifactView.setMessage("No artifacts found in this category.");
            }
        } catch (Exception ex) {
            artifactView.setMessage("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private void handleSearchArtifact() {
        // Get the name of the artifact from the search field
        String artifactName = artifactView.getSearchFieldInput();

        // Search for the artifact using the service
        MuseumArtifact artifact = artifactService.getArtifact(artifactName);

        // If the artifact is found, display its details in the view
        if (artifact != null) {
            // Set all the fields in the view with the artifact details

            artifactView.setArtifactDetails(
                    String.valueOf(artifact.getArtifactId()),
                    artifact.getCategory(),
                    artifact.getDescription(),
                    artifact.getLocationInMuseum(),
                    new SimpleDateFormat("yyyy-MM-dd").format(artifact.getAcquisitionDate())
            );

            artifactView.setMessage(""); // Clear any previous messages
        } else {
            artifactView.setMessage("Artifact not found.");
        }
    }


    private void handleAddArtifact() {
        // Get inputs from the view
        String name = artifactView.getArtifactNameInput();
        String category = artifactView.getArtifactCategoryInput();
        String description = artifactView.getArtifactDescriptionInput();
        Date acquisitionDate = artifactView.getArtifactAcquisitionDateInput();
        String location = artifactView.getArtifactLocationInput();

        // Validate inputs
        if (name.isEmpty() || category.isEmpty() || description.isEmpty() || acquisitionDate == null || location.isEmpty()) {
            artifactView.setMessage("Please fill in all fields."); // Inform user if any field is missing
            return;
        }

        // Create a new MuseumArtifact object with the provided inputs
        MuseumArtifact newArtifact = new MuseumArtifact(name, category, description, acquisitionDate, location);

        try {
            // Add the artifact using the service
            artifactService.addArtifact(newArtifact);
            artifactView.setMessage("Artifact added successfully!"); // Notify success
            artifactView.clearInputFields(); // Clear the input fields after adding the artifact
        } catch (IllegalArgumentException e) {
            artifactView.setMessage(e.getMessage()); // Show error message if any validation fails
        }
    }

}
