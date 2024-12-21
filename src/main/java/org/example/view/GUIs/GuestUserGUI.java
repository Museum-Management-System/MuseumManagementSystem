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
import org.example.view.GUIComponents.ObjectCard;

import java.sql.SQLException;

public class GuestUserGUI extends Application {
    protected Stage primaryStage;
    protected ObjectCard objectCard;
    private TableView<MuseumArtifact> objectTableView;
    private GuestUserController controller;
    private TextField searchField;

    protected String getUserType() {return "Guest";}

    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("User Interface");
        this.primaryStage = primaryStage;
        VBox sidebar = new VBox();
        controller = new GuestUserController(searchField, objectTableView);
        controller.setPrimaryStage(primaryStage);
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
            objectsButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
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
        // Check the type of the current instance and pass the appropriate parameter
        if (this instanceof EmployeeGUI) {
            objectCard = new ObjectCard(objectsPage, getUserType(), (EmployeeGUI) this);
        } else {
            objectCard = new ObjectCard(objectsPage, getUserType(), null);
        }
        objectsPage.setSpacing(15);
        objectsPage.setStyle("-fx-padding: 15px;");

        Label titleLabel = new Label("Search Objects:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Search...");

        Button searchButton = new Button("SEARCH");
        searchButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;");
        searchButton.setOnMouseEntered(event -> searchButton.setStyle("-fx-background-color: #fb6767; -fx-text-fill: white;"));
        searchButton.setOnMouseExited(event -> searchButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;"));

        Button filterButton = new Button("FILTER");
        filterButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;");
        filterButton.setOnMouseEntered(event -> filterButton.setStyle("-fx-background-color: #fb6767; -fx-text-fill: white;"));
        filterButton.setOnMouseExited(event -> filterButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;"));


        Button addObjectButton = new Button("Add New Object");
        addObjectButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white;");
        addObjectButton.setOnMouseEntered(event -> addObjectButton.setStyle("-fx-background-color: #b75cd3; -fx-text-fill: white;"));
        addObjectButton.setOnMouseExited(event -> addObjectButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white;"));

        addObjectButton.setVisible(false);

        HBox buttonsBox = new HBox(searchButton, filterButton);
        buttonsBox.setSpacing(10);

        HBox topBox = new HBox(titleLabel, addObjectButton);
        topBox.setSpacing(15);
        topBox.setStyle("-fx-alignment: center-left;");

        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();

        objectTableView = new TableView<>();

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
        searchField.setOnKeyPressed(e -> controller.handleSearch());
        filterButton.setOnAction(e -> controller.handleFilter());

        objectTableView.setOnMouseClicked(event -> {
            MuseumArtifact selectedObject = objectTableView.getSelectionModel().getSelectedItem();
            if (selectedObject != null) {
                displayObjectCard(selectedObject, root);
            }
        });

        objectsPage.getChildren().addAll(titleLabel, searchField, buttonsBox, topBox, objectTableView);
        root.setCenter(objectsPage);

        controller.populateObjectList(objectTableView);
    }
    protected void displayObjectCard(MuseumArtifact object, BorderPane root) {
        objectCard.updateCard(object);
        root.setCenter(objectCard);
    }
    public TableView getObjectTableView(){
        return objectTableView;
    }
}
