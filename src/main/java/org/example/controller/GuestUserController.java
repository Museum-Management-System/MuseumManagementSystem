package org.example.controller;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.MuseumArtifact;
import org.example.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestUserController {

    private TextField searchField;
    private TableView<MuseumArtifact> objectTableView;
    private MuseumArtifactDAO museumArtifactDAO;
    private Connection connection;

    public GuestUserController(TextField searchField, TableView<MuseumArtifact> objectTableView) throws SQLException {
        this.searchField = searchField;
        this.objectTableView = objectTableView;
        this.connection = DatabaseConnection.getConnection();
        museumArtifactDAO = new MuseumArtifactDAO(connection);
    }

    public void handleSearch() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            System.out.println("Search for: " + searchText);
            // Implement search logic here
        } else {
            System.out.println("Search field is empty.");
        }
    }

    public void handleFilter() {
        System.out.println("Filter button clicked");
        // Implement filter logic here
    }

    public void handleAddObject() {
        System.out.println("Add New Object button clicked");
        // Implement add object logic here
    }

    public void handleObjectSelection(MuseumArtifact selectedObject) {
        System.out.println("Selected object: " + selectedObject);
        // Implement object selection logic here
    }
    public void populateObjectList(TableView<MuseumArtifact> tableViewView) {
        // Fetch data from DAO
        ArrayList<MuseumArtifact> objects = museumArtifactDAO.getAllArtifacts();
        tableViewView.getItems().clear();
        tableViewView.getItems().addAll(objects);
    }
}
