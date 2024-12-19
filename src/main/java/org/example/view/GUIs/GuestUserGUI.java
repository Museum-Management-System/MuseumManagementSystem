package org.example.view.GUIs;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.GuestUserController;
import org.example.entity.MuseumArtifact;

import java.sql.SQLException;

public class GuestUserGUI extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Interface");
        this.primaryStage = primaryStage;
        VBox sidebar = new VBox();
        Button objectsButton = new Button("  OBJECTS   ");
        objectsButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button employeesButton = new Button("EMPLOYEES");
        employeesButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        employeesButton.setVisible(false); // Make the employees button invisible

        sidebar.getChildren().addAll(objectsButton, employeesButton);
        sidebar.setSpacing(15);
        sidebar.setStyle("-fx-background-color: #909090; -fx-padding: 50px 15px 50px 15px;");
        sidebar.setMaxHeight(Double.MAX_VALUE);
        sidebar.setStyle("-fx-background-color: #909090; -fx-padding: 50px 15px 50px 15px; -fx-alignment: center-left;");

        // Create the welcome label
        Label welcomeLabel = new Label("Welcome");

        // Set up the BorderPane
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(welcomeLabel);
        root.setStyle("-fx-background-color: #d6ced6;");

        // Handle button clicks
        objectsButton.setOnAction(e -> {
            try {
                openObjectsPage(primaryStage);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        employeesButton.setOnAction(e -> System.out.println("Employees button clicked"));

        // Set up the scene and stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Museum Management System");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    protected void openObjectsPage(Stage primaryStage) throws SQLException {
        VBox objectsPage = new VBox();
        objectsPage.setSpacing(15);
        objectsPage.setStyle("-fx-padding: 15px;");

        Label titleLabel = new Label("Search Objects:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        Button searchButton = new Button("SEARCH");
        searchButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;");

        Button filterButton = new Button("FILTER");
        filterButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;");

        Button addObjectButton = new Button("Add New Object");
        addObjectButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white;");
        addObjectButton.setVisible(false);

        HBox buttonsBox = new HBox(searchButton, filterButton);
        buttonsBox.setSpacing(10);

        HBox topBox = new HBox(titleLabel, addObjectButton);
        topBox.setSpacing(15);
        topBox.setStyle("-fx-alignment: center-left;");

        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();

        TableView<MuseumArtifact> objectTableView = new TableView<>();

        // Define columns
        TableColumn<MuseumArtifact, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MuseumArtifact, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<MuseumArtifact, String> yearColumn = new TableColumn<>("Acquisition Date");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("acquisitionDate"));

        TableColumn<MuseumArtifact, String> locationColumn = new TableColumn<>("Location in Museum");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("locationInMuseum"));

        objectTableView.getColumns().addAll(nameColumn, categoryColumn, yearColumn, locationColumn);
        objectTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Populate with sample data
        ObservableList<MuseumArtifact> objectList = FXCollections.observableArrayList();
        objectTableView.setItems(objectList);
        // Create and link the controller
        GuestUserController controller = new GuestUserController(searchField, objectTableView);

        // Attach event handlers
        searchButton.setOnAction(e -> controller.handleSearch());
        filterButton.setOnAction(e -> controller.handleFilter());
        addObjectButton.setOnAction(e -> controller.handleAddObject());
        objectTableView.setOnMouseClicked(event -> {
            MuseumArtifact selectedObject = objectTableView.getSelectionModel().getSelectedItem();
            if (selectedObject != null) {
                controller.handleObjectSelection(selectedObject);
                displayObjectCard(selectedObject, root);
            }
        });

        objectsPage.getChildren().addAll(titleLabel, searchField, buttonsBox, topBox, objectTableView);
        root.setCenter(objectsPage);

        controller.populateObjectList(objectTableView);
    }

    private void displayObjectCard(MuseumArtifact object, BorderPane root) {
        VBox objectCard = new VBox();
        objectCard.setSpacing(15);
        objectCard.setStyle("-fx-padding: 15px; -fx-background-color: #f0f0f0;");

        Label objectTitle = new Label("Details for " + object);
        objectTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label descriptionLabel = new Label("Description: This is a detailed view of " + object);
        descriptionLabel.setWrapText(true);

        Button backButton = new Button("BACK");
        backButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            try {
                openObjectsPage(primaryStage);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }); // Go back to the previous page

        objectCard.getChildren().addAll(objectTitle, descriptionLabel, backButton);

        // Set the object card in the center
        root.setCenter(objectCard);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
