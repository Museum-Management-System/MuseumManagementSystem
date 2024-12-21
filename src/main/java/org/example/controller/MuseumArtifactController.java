/*package org.example.controller;

import org.example.entity.MuseumArtifact;
import org.example.service.MuseumArtifactService;
import org.example.view.MuseumArtifactView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
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

        this.artifactView.getUpdateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateArtifact();
            }
        });

        this.artifactView.getRefreshButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadArtifactData(); // Call the method to refresh data
            }
        });


        this.artifactView.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchArtifact();
            }
        });

        this.artifactView.getUpdateArtifactTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Avoid duplicate events during row updates
                int selectedRow = artifactView.getUpdateArtifactTable().getSelectedRow();
                if (selectedRow != -1) {
                    // Read data from the selected row
                    String name = (String) artifactView.getUpdateArtifactTable().getValueAt(selectedRow, 0);
                    String category = (String) artifactView.getUpdateArtifactTable().getValueAt(selectedRow, 1);
                    String description = (String) artifactView.getUpdateArtifactTable().getValueAt(selectedRow, 2);
                    String acquisitionDate = (String) artifactView.getUpdateArtifactTable().getValueAt(selectedRow, 3);
                    String location = (String) artifactView.getUpdateArtifactTable().getValueAt(selectedRow, 4);

                    // Populate the text fields with the selected row's data
                    artifactView.getUpdateArtifactNameInput().setText(name);
                    artifactView.getUpdateArtifactCategoryInput().setText(category);
                    artifactView.getUpdateArtifactDescriptionInput().setText(description);
                    artifactView.getUpdateArtifactAcquisitionDateInput().setText(acquisitionDate);
                    artifactView.getUpdateArtifactLocationInput().setText(location);
                }
            }
        });

        artifactView.getSearchByCategoryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchByCategory();
            }
        });
        // Action listeners
    }




    private void handleSearchByCategory() {
        try {
            // Get the category input from the view
            String category = artifactView.getSearchCategoryFieldInput();

            // Validate that the category is not empty
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
                            String.valueOf(artifact.getArtifactId()),
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
                // No artifacts found; clear the table and display an error message
                artifactView.clearSearchCategoryField();
                artifactView.clearCategoryTable();
                artifactView.setMessage("No artifacts found in the specified category.");
            }
        } catch (Exception ex) {
            // Handle any exceptions and display an error message
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

    private void handleUpdateArtifact() {
        // Get inputs from the view
        String name = artifactView.getUpdateArtifactNameText();
        String category = artifactView.getUpdateArtifactCategoryText();
        String description = artifactView.getUpdateArtifactDescriptionText();
        String acquisitionDateString = artifactView.getUpdateArtifactAcquisitionDateText();
        String location = artifactView.getUpdateArtifactLocationText();

        // Validate inputs
        if (name.isEmpty() || category.isEmpty() || description.isEmpty() || acquisitionDateString.isEmpty() || location.isEmpty()) {
            artifactView.setMessage("Please fill in all fields."); // Inform user if any field is missing
            return;
        }

        // Convert acquisitionDateString to Date
        Date acquisitionDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Define the expected date format
            acquisitionDate = sdf.parse(acquisitionDateString); // Parse the string into Date
        } catch (ParseException e) {
            artifactView.setMessage("Invalid acquisition date format. Please use yyyy-MM-dd."); // Handle invalid date format
            return;
        }
        java.sql.Date sqlAcquisitionDate = new java.sql.Date(acquisitionDate.getTime());

// Create the MuseumArtifact object
        MuseumArtifact updatedArtifact = new MuseumArtifact(name, category, description, sqlAcquisitionDate, location);

        // Create a MuseumArtifact object with the provided inputs
        //MuseumArtifact updatedArtifact = new MuseumArtifact(name, category, description, acquisitionDate, location);

        try {
            // Update the artifact using the service

            artifactService.updateArtifact(updatedArtifact); // Assume updateArtifact method exists in artifactService
            artifactView.setMessage("Artifact updated successfully!"); // Notify success
            artifactView.clearUpdatePanelInputFields(); // Clear the input fields after updating the artifact
        } catch (IllegalArgumentException e) {
            artifactView.setMessage(e.getMessage()); // Show error message if any validation fails
        }
    }
    private void loadArtifactData() {
        try {
            // Retrieve artifacts from the service
            List<MuseumArtifact> artifacts = artifactService.getAllArtifacts();

            // Clear existing rows
            artifactView.getTableModel().setRowCount(0);

            // Add each artifact as a row in the table
            for (MuseumArtifact artifact : artifacts) {
                Object[] rowData = {
                        //artifact.getArtifactId(),
                        artifact.getName(),
                        artifact.getCategory(),
                        artifact.getDescription(),
                        new SimpleDateFormat("yyyy-MM-dd").format(artifact.getAcquisitionDate()),
                        artifact.getLocationInMuseum()
                };
                artifactView.getTableModel().addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(artifactView, "Error loading artifact data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
*/