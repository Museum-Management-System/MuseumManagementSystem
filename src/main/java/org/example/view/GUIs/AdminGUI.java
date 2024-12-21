package org.example.view.GUIs;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.AdminController;
import org.example.entity.Employee;
import org.example.view.GUIComponents.EmployeeCard;

import java.sql.SQLException;

public class AdminGUI extends EmployeeGUI {
    private AdminController admincontroller;
    private TableView<Employee> employeeTableView;
    private EmployeeCard employeeCard;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Interface");
        this.primaryStage = primaryStage;
        try {
            admincontroller = new AdminController(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        admincontroller.setPrimaryStage(primaryStage);

        VBox sidebar = new VBox();
        Button objectsButton = new Button("  OBJECTS   ");
        objectsButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button employeesButton = new Button("EMPLOYEES");
        employeesButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        employeesButton.setVisible(true); // Make the employees button visible for Admin

        sidebar.getChildren().addAll(objectsButton, employeesButton);
        sidebar.setSpacing(15);
        sidebar.setStyle("-fx-background-color: #909090; -fx-padding: 50px 15px 50px 15px; -fx-alignment: center-left;");

        // Create the welcome label
        Label welcomeLabel = new Label("Welcome, Admin");

        // Set up the BorderPane
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(welcomeLabel);
        root.setStyle("-fx-background-color: #d6ced6;");

        // Handle button clicks
        objectsButton.setOnAction(e -> {
            objectsButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            employeesButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            try {
                openObjectsPage(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        employeesButton.setOnAction(e -> {
            objectsButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            employeesButton.setStyle("-fx-background-color: #dd6dfd; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            try {
                openEmployeesPage(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Set up the scene and stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Museum Management System - Admin");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    protected void openEmployeesPage(Stage primaryStage) throws SQLException {
        VBox employeesPage = new VBox();
        employeesPage.setSpacing(15);
        employeesPage.setStyle("-fx-padding: 15px;");
        employeeCard = new EmployeeCard(employeesPage, this);

        Label titleLabel = new Label("Search Employees:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setId("searchField");
        searchField.setPromptText("Search...");

        Button searchButton = new Button("SEARCH");
        searchButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;");
        searchButton.setOnMouseEntered(event -> searchButton.setStyle("-fx-background-color: #fb6767; -fx-text-fill: white;"));
        searchButton.setOnMouseExited(event -> searchButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;"));


        Button filterButton = new Button("FILTER");
        filterButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;");
        filterButton.setOnMouseEntered(event -> filterButton.setStyle("-fx-background-color: #fb6767; -fx-text-fill: white;"));
        filterButton.setOnMouseExited(event -> filterButton.setStyle("-fx-background-color: #ff9999; -fx-text-fill: white;"));

        Button addEmployeeButton = new Button("Add New Employee");
        addEmployeeButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white;");
        addEmployeeButton.setOnMouseEntered(event -> addEmployeeButton.setStyle("-fx-background-color: #b75cd3; -fx-text-fill: white;"));
        addEmployeeButton.setOnMouseExited(event -> addEmployeeButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white;"));
        addEmployeeButton.setOnAction(e -> System.out.println("Add New Employee button clicked!"));


        HBox buttonsBox = new HBox(searchButton, filterButton, addEmployeeButton); // Add the button to the HBox
        buttonsBox.setSpacing(10);

        employeeTableView = new TableView<>();

        // Define columns
        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Employee, String> phoneColumn = new TableColumn<>("Phone Number");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

        TableColumn<Employee, String> titleColumn = new TableColumn<>("Job Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        TableColumn<Employee, String> sectionColumn = new TableColumn<>("Section");
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("sectionName"));

        employeeTableView.getColumns().addAll(nameColumn, emailColumn, phoneColumn, titleColumn, sectionColumn);
        employeeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set up event handlers
        employeeTableView.setOnMouseClicked(event -> {
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                displayEmployeeCard(selectedEmployee, (BorderPane) primaryStage.getScene().getRoot());
            }
        });

        searchButton.setOnAction(e -> admincontroller.handleSearch());
        searchField.setOnKeyPressed(e -> admincontroller.handleSearch());
        filterButton.setOnAction(e -> admincontroller.handleFilter());

        addEmployeeButton.setOnAction(e -> admincontroller.handleAddEmployee());

        employeesPage.getChildren().addAll(titleLabel, searchField, buttonsBox, employeeTableView);

        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        root.setCenter(employeesPage);

        admincontroller.populateEmployeeList();
    }
    protected void displayEmployeeCard(Employee employee, BorderPane root) {
        employeeCard.updateCard(employee);
        root.setCenter(employeeCard);
    }
    @Override
    protected String getUserType() { return "Admin";}
    public TableView getEmployeeTableView(){
        return employeeTableView;
    }
}
