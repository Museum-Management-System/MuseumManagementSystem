package org.example.controller;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.MuseumArtifact;
import org.example.service.DatabaseConnection;
import org.example.view.GUIComponents.Filter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.stage.Stage;

public class GuestUserController {

    private TextField searchField;
    private TableView<MuseumArtifact> objectTableView;
    private MuseumArtifactDAO museumArtifactDAO;
    private Connection connection;
    protected Stage primaryStage;


    public GuestUserController(TextField searchField, TableView<MuseumArtifact> objectTableView) throws SQLException {
        this.searchField = searchField;
        this.objectTableView = objectTableView;
        this.connection = DatabaseConnection.getConnection();
        museumArtifactDAO = new MuseumArtifactDAO(connection);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void handleSearch() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            System.out.println("Search for: " + searchText);
            ArrayList<MuseumArtifact> searchResults = museumArtifactDAO.searchArtifacts(searchText);
            objectTableView.getItems().clear();
            objectTableView.getItems().addAll(searchResults);
        } else {
            System.out.println("Search field is empty. Displaying all artifacts.");
            populateObjectList(objectTableView);
        }
    }

    public void handleFilter() {
        ArrayList<String> categories = museumArtifactDAO.getAllCategories();
        ArrayList<String> locations = museumArtifactDAO.getAllLocations();

        Filter filter = new Filter(categories, locations);
        filter.show(primaryStage, () -> {
            ArrayList<String> selectedCategories = filter.getSelectedCategories();
            ArrayList<String> selectedLocations = filter.getSelectedLocations();
            String minDate = filter.getMinDate();
            String maxDate = filter.getMaxDate();

            ArrayList<MuseumArtifact> filteredResults = museumArtifactDAO.filterArtifacts(selectedCategories, minDate, maxDate, selectedLocations);
            objectTableView.getItems().clear();
            objectTableView.getItems().addAll(filteredResults);
        });
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
