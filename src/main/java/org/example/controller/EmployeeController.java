package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.MuseumArtifact;
import org.example.service.DatabaseConnection;
import org.example.service.MuseumArtifactService;
import org.example.view.GUIs.EmployeeGUI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeController extends GuestUserController{
    private MuseumArtifactService artifactService;
    private MuseumArtifactDAO museumArtifactDAO;
    private Connection connection;
    private EmployeeGUI empGUI;
    public EmployeeController(EmployeeGUI empGUI) throws SQLException {
        super(null, null);
        this.connection = DatabaseConnection.getConnection();
        this.museumArtifactDAO = new MuseumArtifactDAO(connection);
        this.artifactService = new MuseumArtifactService(museumArtifactDAO);
        this.empGUI = empGUI;
    }
    public void handleAddObject(MuseumArtifact addedObject) {
        try {
            if (artifactService.addArtifact(addedObject)) {
                System.out.println("Artifact added successfully: " + addedObject.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Successful");
                alert.setHeaderText(null);
                alert.setContentText("Artifact added successfully!");
                alert.showAndWait();

                // Refresh the object table
                populateObjectList();
            } else {
                System.out.println("Failed to add artifact: " + addedObject.getName());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add artifact. Please try again.");
                alert.showAndWait();
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void handleUpdateObject(MuseumArtifact updatedObject) {
        try {
            if (artifactService.updateArtifact(updatedObject)) {
                System.out.println("Artifact updated successfully: " + updatedObject.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setHeaderText(null);
                alert.setContentText("Artifact updated successfully!");
                alert.showAndWait();

                // Refresh the object table
                populateObjectList();
            } else {
                System.out.println("Failed to update artifact: " + updatedObject.getName());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update artifact. Please try again.");
                alert.showAndWait();
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /*
     * Handle deleting an object.
     */
    public void handleDeleteObject(MuseumArtifact selectedObject) {
        if (selectedObject != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete the selected artifact?");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean success = artifactService.deleteArtifact(selectedObject.getName());
                    if (success) {
                        System.out.println("Artifact deleted successfully: " + selectedObject.getName());
                        showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Artifact deleted successfully!");
                        populateObjectList();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete artifact. Please try again.");
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No artifact selected for deletion.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(message); alert.showAndWait();
    }

    /*
     * Populate the object list in the table view.
     */
    public void populateObjectList() {
        ArrayList<MuseumArtifact> allArtifacts = museumArtifactDAO.getAllArtifacts();
        empGUI.getObjectTableView().getItems().clear();
        empGUI.getObjectTableView().getItems().addAll(allArtifacts);
    }
}
