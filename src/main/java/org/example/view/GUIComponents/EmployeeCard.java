package org.example.view.GUIComponents;
import javafx.application.Platform;
import org.example.controller.AdminController;
import org.example.entity.Employee;
import org.example.service.DatabaseConnection;
import java.sql.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.example.view.GUIs.AdminGUI;

//That's the interface when Admin clicks on an employee from List

public class EmployeeCard extends BorderPane {

    private boolean isEditing = false;
    private DatabaseConnection connection;
    private AdminController controller;
    private ImageView employeeImageView;
    private Employee employee;
    private TextField nameField, roleField, sectionField, emailField, phoneField;
    private Label employeeNameLabel;

    public EmployeeCard(VBox previousPage, AdminGUI adminGUI) throws SQLException {
        // Root Pane
        controller = new AdminController(adminGUI);

        // Top Section: Employee Name and Image
        VBox topLeftSection = new VBox();
        topLeftSection.setSpacing(10);
        topLeftSection.setAlignment(Pos.TOP_LEFT);

        // Employee Name
        employeeNameLabel = new Label("Employee Name");
        employeeNameLabel.setFont(Font.font("Arial", 18));
        employeeNameLabel.setStyle("-fx-background-color: #c4a7a7; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
        employeeNameLabel.setMaxWidth(Double.MAX_VALUE);
        employeeNameLabel.setAlignment(Pos.CENTER_LEFT);

        // Employee Image
        employeeImageView = new ImageView();
        employeeImageView.setFitWidth(150);
        employeeImageView.setFitHeight(150);
        employeeImageView.setPreserveRatio(true);
        employeeImageView.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c4c4c4; -fx-border-radius: 10; -fx-background-radius: 10;");
        loadImageAsync("https://themeisle.com/blog/wp-content/uploads/2024/06/Online-Image-Optimizer-Test-Image-JPG-Version.jpeg");

        // Add Name and Image to Top Section
        topLeftSection.getChildren().addAll(employeeNameLabel, employeeImageView);

        // Right Section: Editable Employee Details
        VBox detailsBox = new VBox();
        detailsBox.setAlignment(Pos.TOP_LEFT);

        // Editable fields
        Label nameLabel = new Label("Name: ");
        nameField = createEditableField("John Doe");

        Label roleLabel = new Label("Role: ");
        roleField = createEditableField("Software Engineer");

        Label sectionLabel = new Label("Section: ");
        sectionField = createEditableField("IT Department");

        Label emailLabel = new Label("Email: ");
        emailField = createEditableField("xxx@gmail.com");

        Label phoneLabel = new Label("Phone Number: ");
        phoneField = createEditableField("0123 456 78 90");

        // Arrange Labels and TextFields Horizontally
        HBox nameBox = new HBox(nameLabel, nameField);
        HBox roleBox = new HBox(roleLabel, roleField);
        HBox departmentBox = new HBox(sectionLabel, sectionField);
        HBox emailBox = new HBox(emailLabel, emailField);
        HBox phoneBox = new HBox(phoneLabel, phoneField);

        nameBox.setAlignment(Pos.BASELINE_LEFT);
        roleBox.setAlignment(Pos.BASELINE_LEFT);
        departmentBox.setAlignment(Pos.BASELINE_LEFT);
        emailBox.setAlignment(Pos.BASELINE_LEFT);
        phoneField.setAlignment(Pos.BASELINE_LEFT);

        nameBox.setTranslateY(-2);  // Moves up by 2 pixels
        roleBox.setTranslateY(-2);  // Moves up by 2 pixels
        departmentBox.setTranslateY(-2);  // Moves up by 2 pixels
        emailBox.setTranslateY(-2);  // Moves up by 2 pixels
        phoneLabel.setTranslateY(4);  // Moves down by 4 pixels

        detailsBox.getChildren().addAll(nameBox, roleBox, departmentBox, emailBox, phoneBox);
        detailsBox.setTranslateY(40); // Adjust the value (positive to lower)

        // Bottom Section: Buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        Button updateSaveButton = new Button("Update Employee");
        updateSaveButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        updateSaveButton.setOnMouseEntered(e -> updateSaveButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        updateSaveButton.setOnMouseExited(e -> updateSaveButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));

        Button deleteButton = new Button("Delete Employee");
        deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        backButton.setOnAction(event -> ((BorderPane)this.getParent()).setCenter(previousPage));

        // Button Actions
        updateSaveButton.setOnAction(event -> {
            if (!isEditing) {
                // Enter editing mode
                isEditing = true;
                updateSaveButton.setText("Save Employee");

                // Enable editing for all fields
                setEditable(nameField, true);
                setEditable(roleField, true);
                setEditable(sectionField, true);
                setEditable(emailField, false);
                setEditable(phoneField, true);
            } else {
                // Save and exit editing mode
                isEditing = false;
                updateSaveButton.setText("Update Employee");

                // Disable editing for all fields
                setEditable(nameField, false);
                setEditable(roleField, false);
                setEditable(sectionField, false);
                setEditable(emailField, false);
                setEditable(phoneField, false);

                controller.handleEditEmployee(nameField.getText(), roleField.getText(),
                        sectionField.getText(), emailField.getText(),phoneField.getText());
            }
        });

        deleteButton.setOnAction(event -> {
            // Confirmation Dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Delete Confirmation");
            confirmationDialog.setHeaderText("Are you sure you want to delete?");
            confirmationDialog.setContentText("This action cannot be undone.");

            // Show and wait for user response
            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // If user confirms, delete the card and show success message
                    ((BorderPane)this.getParent()).setCenter(previousPage);

                    controller.handleDeleteEmployee(employee);
                }
            });
        });

        // Add buttons to bottom section
        buttonBox.getChildren().addAll(updateSaveButton, deleteButton, backButton);
        buttonBox.setTranslateY(-300);

        // Arrange sections in the root layout
        HBox topSection = new HBox(topLeftSection, detailsBox);
        topSection.setSpacing(20);

        this.setTop(topSection);
        this.setBottom(buttonBox);

        BorderPane.setMargin(topSection, new Insets(10));
        BorderPane.setMargin(buttonBox, new Insets(10));
    }
    private void loadImageAsync(String imageUrl) {
        new Thread(() -> {
            Image image = new Image(imageUrl);
            Platform.runLater(() -> employeeImageView.setImage(image));
        }).start();
    }
    private TextField createEditableField(String initialText) {
        TextField field = new TextField(initialText);
        setEditable(field, false);
        return field;
    }

    private void setEditable(TextField field, boolean editable) {
        field.setEditable(editable);
        if (editable) {
            field.setStyle("-fx-control-inner-background: #ffffff; -fx-border-color: #c4c4c4;");
        } else {
            field.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        }
    }
    public void updateCard(Employee employee) {
        this.employee = employee;
        employeeNameLabel.setText(employee.getName());
        //objectImageView.setImage(new Image(artifact.getImageUrl(), 150, 150, true, true)); // Adjust as needed
        nameField.setText(employee.getName());
        sectionField.setText(employee.getSectionName());
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhoneNum());
    }
}
