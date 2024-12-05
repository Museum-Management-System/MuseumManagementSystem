package org.example.controller;

import org.example.entity.MuseumArtifact;
import org.example.service.MuseumArtifactService;
import org.example.view.MuseumArtifactView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
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

    private void handleSearchArtifacts() {
        try {
            String artifactName = artifactView.getSearchFieldInput();
            if (artifactName == null || artifactName.isEmpty()) {
                throw new IllegalArgumentException("Artifact name is required.");
            }

            MuseumArtifact artifact = artifactService.getArtifact(artifactName);

            if (artifact != null) {
                artifactView.setArtifactDetails(
                        String.valueOf(artifact.getArtifactId()),
                        artifact.getCategory(),
                        artifact.getDescription(),
                        artifact.getLocationInMuseum(),
                        new SimpleDateFormat("yyyy-MM-dd").format(artifact.getAcquisitionDate())
                );
            } else {
                JOptionPane.showMessageDialog(artifactView, "Artifact not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(artifactView, "Error searching for artifact: " + ex.getMessage());
        }
    }
}
