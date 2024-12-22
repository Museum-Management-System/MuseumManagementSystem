package org.example.service;

import org.example.dao.MuseumArtifactDAO;
import org.example.entity.MuseumArtifact;

import java.util.List;

public class MuseumArtifactService {

    //business logic will be implemented
    private MuseumArtifactDAO artifactDAO;

    public MuseumArtifactService(MuseumArtifactDAO artifactDAO) {
        this.artifactDAO = artifactDAO;
    }

    public boolean addArtifact(MuseumArtifact artifact) {
        if (artifact.getName() == null || artifact.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (artifact.getCategory() == null || artifact.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }

        if (artifact.getDescription() == null || artifact.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }

        if (artifact.getAcquisitionDate() == null) {
            throw new IllegalArgumentException("Acquisition Date is required.");
        }

        if (artifact.getLocationInMuseum() == null || artifact.getLocationInMuseum().isEmpty()) {
            throw new IllegalArgumentException("Location in Museum is required.");
        }

        // Check if an artifact with the same name already exists
        if (artifactDAO.getArtifact(artifact.getName()) != null) {
            throw new IllegalArgumentException("An artifact with this name already exists.");
        }

        return artifactDAO.addArtifact(artifact);
    }


    public MuseumArtifact getArtifact(String artifactName){
        return artifactDAO.getArtifact(artifactName);
    }

    public boolean deleteArtifact(String artifactName) {

        if (artifactName == null || artifactName.isEmpty()) {
            throw new IllegalArgumentException("Artifact name must not be null or empty.");
        }

        // Check if the artifact exists
        MuseumArtifact existingArtifact = artifactDAO.getArtifact(artifactName);
        if (existingArtifact == null) {
            throw new IllegalArgumentException("The artifact with the specified name does not exist.");
        }

        return artifactDAO.deleteArtifact(artifactName);
    }

    public boolean updateArtifact(MuseumArtifact artifact) {
        MuseumArtifact existingArtifact = artifactDAO.getArtifact(artifact.getName());
        if (existingArtifact != null && existingArtifact.getArtifactId() != artifact.getArtifactId()) {
            throw new IllegalArgumentException("An artifact with this name already exists.");
        }
        return artifactDAO.updateArtifact(artifact);
    }

    public List<MuseumArtifact> searchArtifacts(String name) {
        return artifactDAO.searchArtifacts(name);
    }
    public List<MuseumArtifact> searchArtifactsByCategory(String category) {
        return artifactDAO.searchArtifactsByCategory(category);
    }
    public List<MuseumArtifact> getAllArtifacts(){
        return artifactDAO.getAllArtifacts();
    }

}
