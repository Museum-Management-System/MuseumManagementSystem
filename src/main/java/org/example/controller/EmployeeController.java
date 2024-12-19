package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.MuseumArtifact;
import org.example.service.DatabaseConnection;
import org.example.service.MuseumArtifactService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeController {

    private TextField searchField;
    private TableView<MuseumArtifact> objectTableView;
    private MuseumArtifactService artifactService;
    private MuseumArtifactDAO museumArtifactDAO;
    private Connection connection;

    public EmployeeController(TextField searchField, TableView<MuseumArtifact> objectTableView) throws SQLException {
        this.searchField = searchField;
        this.objectTableView = objectTableView;
        this.connection = DatabaseConnection.getConnection();
        this.museumArtifactDAO = new MuseumArtifactDAO(connection);
        this.artifactService = new MuseumArtifactService(museumArtifactDAO);
    }

    /*
     * Handle searching for objects by name or category.
     */
    public void handleSearch() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            System.out.println("Search for: " + searchText);
            ArrayList<MuseumArtifact> filteredArtifacts = museumArtifactDAO.searchArtifacts(searchText);
            objectTableView.getItems().clear();
            objectTableView.getItems().addAll(filteredArtifacts);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Search field cannot be empty.");
            alert.showAndWait();
        }
    }

    /*
     * Handle filtering artifacts (e.g., by acquisition date or category).
     */
    public void handleFilter(String filterType, String filterValue) {
        System.out.println("Filtering by: " + filterType + " with value: " + filterValue);
        ArrayList<MuseumArtifact> filteredArtifacts;

        if (filterType.equalsIgnoreCase("acquisition_date")) {
            filteredArtifacts = museumArtifactDAO.acquisitionDateFiltering(filterValue);
        } else if (filterType.equalsIgnoreCase("category")) {
            filteredArtifacts = museumArtifactDAO.searchArtifacts(filterValue);
        } else {
            filteredArtifacts = new ArrayList<>();
        }

        objectTableView.getItems().clear();
        objectTableView.getItems().addAll(filteredArtifacts);
    }

    /*
     * Handle updating an object's details.
     */
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
                /*if (response == Alert.AlertType) {
                    if (artifactService.deleteArtifact(selectedObject.getName())) {
                        System.out.println("Artifact deleted successfully: " + selectedObject.getName());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Deletion Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Artifact deleted successfully!");
                        alert.showAndWait();

                        // Refresh the object table
                        populateObjectList();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Deletion Failed");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to delete artifact. Please try again.");
                        alert.showAndWait();
                    }
                } */
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No artifact selected for deletion.");
            alert.showAndWait();
        }
    }

    /*
     * Handle selecting an object in the table.
     */
    public void handleObjectSelection(MouseEvent event) {
        MuseumArtifact selectedArtifact = objectTableView.getSelectionModel().getSelectedItem();
        if (selectedArtifact != null) {
            System.out.println("Selected object: " + selectedArtifact);
            // Implement further actions for selection if needed
        }
    }

    /*
     * Populate the object list in the table view.
     */
    public void populateObjectList() {
        ArrayList<MuseumArtifact> allArtifacts = museumArtifactDAO.getAllArtifacts();
        objectTableView.getItems().clear();
        objectTableView.getItems().addAll(allArtifacts);
    }
}
