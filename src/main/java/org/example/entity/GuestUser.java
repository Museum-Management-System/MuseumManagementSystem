package org.example.entity;

import org.example.service.MuseumArtifactService;
import org.example.entity.MuseumArtifact;

import java.util.List;
public class GuestUser {
    private MuseumArtifactService artifactService;

    public GuestUser(MuseumArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    public List<MuseumArtifact> searchArtifacts(String objectName) {
        return artifactService.searchArtifacts(objectName); // Delegates to MuseumArtifactService
    }

    public List<MuseumArtifact> filterArtifactsByCategory(String category) {
        return artifactService.searchArtifacts(category); // Filters artifacts by category
    }

    public void displayArtifacts(List<MuseumArtifact> artifacts) {
        if (artifacts == null || artifacts.isEmpty()) {
            System.out.println("No artifacts found.");
            return;
        }

        for (MuseumArtifact artifact : artifacts) {
            System.out.printf("Name: %s, Category: %s, Description: %s, Acquisition Date: %s, Location: %s%n",
                    artifact.getName(),
                    artifact.getCategory(),
                    artifact.getDescription(),
                    artifact.getAcquisitionDate(),
                    artifact.getLocationInMuseum());
        }
    }
}
