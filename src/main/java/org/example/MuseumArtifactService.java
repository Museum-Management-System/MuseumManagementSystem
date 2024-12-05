package org.example;

import java.util.List;

public class MuseumArtifactService {

    //business logic will be implemented
    private MuseumArtifactDAO artifactDAO;

    public MuseumArtifactService(MuseumArtifactDAO artifactDAO) {
        this.artifactDAO = artifactDAO;
    }

    public void addArtifact(MuseumArtifact artifact) {
        if (artifact.getName() == null || artifact.getName().isEmpty()
                || artifact.getCategory() == null || artifact.getCategory().isEmpty()
                || artifact.getDescription() == null || artifact.getDescription().isEmpty()
                || artifact.getAcquisitionDate() == null
                || artifact.getLocationInMuseum() == null || artifact.getLocationInMuseum().isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }

        if (artifactDAO.getArtifact(artifact.getName()) != null) {
            throw new IllegalArgumentException("An artifact with this name already exists.");
        }

        artifactDAO.addArtifact(artifact);
    }

    public MuseumArtifact getArtifact(String artifactName){
        return artifactDAO.getArtifact(artifactName);
    }

    public boolean deleteArtifact(String artifactName) {
        return artifactDAO.deleteArtifact(artifactName);
    }

    public boolean updateArtifact(MuseumArtifact artifact) {
        MuseumArtifact existingArtifact = artifactDAO.getArtifact(artifact.getName());
        if (existingArtifact != null && existingArtifact.getArtifactId() != artifact.getArtifactId()) {
            throw new IllegalArgumentException("An artifact with this name already exists.");
        }
        return artifactDAO.updateArtifact(artifact);
    }

    public List<MuseumArtifact> searchArtifacts(String category) {
        return artifactDAO.searchArtifacts(category);
    }

}
