package org.example.controller;

import org.example.MuseumArtifact;
import org.example.MuseumArtifactService;
import org.example.view.MuseumArtifactView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MuseumArtifactController {
    private MuseumArtifactView artifactView;
    private MuseumArtifactService artifactService;

    public MuseumArtifactController(MuseumArtifactView artifactView, MuseumArtifactService artifactService) {
        this.artifactView = artifactView;
        this.artifactService = artifactService;

        initController();
    }

    private void initController() {
        artifactView.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddArtifact();
            }
        });

        artifactView.getUpdateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateArtifact();
            }
        });

        artifactView.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteArtifact();
            }
        });

        artifactView.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchArtifacts();
            }
        });
    }

    private void handleAddArtifact() {
        try {
            String name = JOptionPane.showInputDialog("Enter Artifact Name:");
            String category = JOptionPane.showInputDialog("Enter Artifact Category:");
            String description = JOptionPane.showInputDialog("Enter Artifact Description:");
            String location = JOptionPane.showInputDialog("Enter Artifact Location:");
            String acquisitionDateInput = JOptionPane.showInputDialog("Enter Acquisition Date (YYYY-MM-DD):");

            java.sql.Date acquisitionDate = java.sql.Date.valueOf(acquisitionDateInput);

            MuseumArtifact artifact = new MuseumArtifact(name, category, description, acquisitionDate, location);
            artifactService.addArtifact(artifact);

            JOptionPane.showMessageDialog(artifactView, "Artifact added successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(artifactView, "Error adding artifact: " + ex.getMessage());
        }
    }

    private void handleUpdateArtifact() {
        try {
            String existingName = JOptionPane.showInputDialog("Enter the Name of the Artifact to Update:");
            MuseumArtifact existingArtifact = artifactService.getArtifact(existingName);

            if (existingArtifact == null) {
                JOptionPane.showMessageDialog(artifactView, "Artifact not found!");
                return;
            }

            String name = JOptionPane.showInputDialog("Enter New Name:", existingArtifact.getName());
            String category = JOptionPane.showInputDialog("Enter New Category:", existingArtifact.getCategory());
            String description = JOptionPane.showInputDialog("Enter New Description:", existingArtifact.getDescription());
            String location = JOptionPane.showInputDialog("Enter New Location:", existingArtifact.getLocationInMuseum());
            String acquisitionDateInput = JOptionPane.showInputDialog("Enter New Acquisition Date (YYYY-MM-DD):", existingArtifact.getAcquisitionDate());

            java.sql.Date acquisitionDate = java.sql.Date.valueOf(acquisitionDateInput);

            MuseumArtifact updatedArtifact = new MuseumArtifact(name, category, description, acquisitionDate, location);
            updatedArtifact.setArtifactId(existingArtifact.getArtifactId());

            artifactService.updateArtifact(updatedArtifact);

            JOptionPane.showMessageDialog(artifactView, "Artifact updated successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(artifactView, "Error updating artifact: " + ex.getMessage());
        }
    }

    private void handleDeleteArtifact() {
        try {
            String name = JOptionPane.showInputDialog("Enter the Name of the Artifact to Delete:");
            boolean isDeleted = artifactService.deleteArtifact(name);

            if (isDeleted) {
                JOptionPane.showMessageDialog(artifactView, "Artifact deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(artifactView, "Artifact not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(artifactView, "Error deleting artifact: " + ex.getMessage());
        }
    }

    private void handleSearchArtifacts() {
        try {
            String category = JOptionPane.showInputDialog("Enter the Category of Artifacts to Search:");
            List<MuseumArtifact> artifacts = artifactService.searchArtifacts(category);

            if (artifacts.isEmpty()) {
                JOptionPane.showMessageDialog(artifactView, "No artifacts found in this category.");
            } else {
                StringBuilder result = new StringBuilder("Artifacts found:\n");
                for (MuseumArtifact artifact : artifacts) {
                    result.append("Name: ").append(artifact.getName())
                            .append(", Location: ").append(artifact.getLocationInMuseum()).append("\n");
                }
                JOptionPane.showMessageDialog(artifactView, result.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(artifactView, "Error searching artifacts: " + ex.getMessage());
        }
    }
}
