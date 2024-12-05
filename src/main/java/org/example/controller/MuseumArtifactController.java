package org.example.controller;

import org.example.entity.MuseumArtifact;
import org.example.service.MuseumArtifactService;
import org.example.view.MuseumArtifactView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

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
