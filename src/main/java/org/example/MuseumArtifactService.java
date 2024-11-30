package org.example;

import java.util.List;

public class MuseumArtifactService {

    //business logic will be implemented
    private MuseumArtifactDAO artifactDAO;

    public MuseumArtifactService(MuseumArtifactDAO artifactDAO) {
        this.artifactDAO = artifactDAO;
    }

    public void addArtifact(MuseumArtifact artifact) {
        artifactDAO.addArtifact(artifact);
    }

    public MuseumArtifact getArtifact(String artifactName){
        return artifactDAO.getArtifact(artifactName);
    }

    public boolean deleteArtifact(String artifactName) {
        return artifactDAO.deleteArtifact(artifactName);
    }

    public boolean updateArtifact(MuseumArtifact artifact) {
        return artifactDAO.updateArtifact(artifact);
    }

    public List<MuseumArtifact> searchArtifacts(String category) {
        return artifactDAO.searchArtifacts(category);
    }

}
