package com.example.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EmployeeCard extends Application {

    private boolean isEditing = false; // Flag to toggle between Update and Save

    @Override
    public void start(Stage primaryStage) {
        // Root Pane
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #d3d3d3;");


        // Top Section: Employee Name and Image
        VBox topLeftSection = new VBox();
        topLeftSection.setSpacing(10);
        topLeftSection.setAlignment(Pos.TOP_LEFT);

        // Employee Name
        Label employeeNameLabel = new Label("Employee Name");
        employeeNameLabel.setFont(Font.font("Arial", 18));
        employeeNameLabel.setStyle("-fx-background-color: #c4a7a7; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
        employeeNameLabel.setMaxWidth(Double.MAX_VALUE);
        employeeNameLabel.setAlignment(Pos.CENTER_LEFT);

        // Employee Image
        ImageView employeeImageView = new ImageView();
        employeeImageView.setFitWidth(150);
        employeeImageView.setFitHeight(150);
        employeeImageView.setPreserveRatio(true);
        employeeImageView.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c4c4c4; -fx-border-radius: 10; -fx-background-radius: 10;");
        employeeImageView.setImage(new Image("https://via.placeholder.com/150"));

        // Add Name and Image to Top Section
        topLeftSection.getChildren().addAll(employeeNameLabel, employeeImageView);

        // Right Section: Editable Employee Details
        VBox detailsBox = new VBox();
        detailsBox.setAlignment(Pos.TOP_LEFT);

        // Editable fields
        Label nameLabel = new Label("Name: ");
        TextField nameField = createEditableField("John Doe");

        Label roleLabel = new Label("Role: ");
        TextField roleField = createEditableField("Software Engineer");

        Label departmentLabel = new Label("Department: ");
        TextField departmentField = createEditableField("IT Department");

        Label emailLabel = new Label("Email: ");
        TextField emailField = createEditableField("xxx@gmail.com");

        Label phoneLabel = new Label("Phone Number: ");
        TextField phoneField = createEditableField("0123 456 78 90");

        // Arrange Labels and TextFields Horizontally
        HBox nameBox = new HBox(nameLabel, nameField);
        HBox roleBox = new HBox(roleLabel, roleField);
        HBox departmentBox = new HBox(departmentLabel, departmentField);
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

        Button updateSaveButton = new Button("Update Object");
        updateSaveButton.setStyle("-fx-background-color: #c47979; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        updateSaveButton.setOnMouseEntered(e -> updateSaveButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        updateSaveButton.setOnMouseExited(e -> updateSaveButton.setStyle("-fx-background-color: #c47979; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));

        Button deleteButton = new Button("Delete Object");
        deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));

        // Button Actions
        updateSaveButton.setOnAction(event -> {
            if (!isEditing) {
                // Enter editing mode
                isEditing = true;
                updateSaveButton.setText("Save Employee");

                // Enable editing for all fields
                setEditable(nameField, true);
                setEditable(roleField, true);
                setEditable(departmentField, true);
                setEditable(emailField, true);
                setEditable(phoneField, true);
            } else {
                // Save and exit editing mode
                isEditing = false;
                updateSaveButton.setText("Update Employee");

                // Disable editing for all fields
                setEditable(nameField, false);
                setEditable(roleField, false);
                setEditable(departmentField, false);
                setEditable(emailField, false);
                setEditable(phoneField, false);

                // Save the updated details (Print to console for now)
                System.out.println("Updated Name: " + nameField.getText());
                System.out.println("Updated Role: " + roleField.getText());
                System.out.println("Updated Department: " + departmentField.getText());
                System.out.println("Updated Email: " + emailField.getText());
                System.out.println("Updated Phone Number: " + phoneField.getText());
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
                    primaryStage.close();  // Close the Employee Card window

                    Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                    successDialog.setTitle("Success");
                    successDialog.setHeaderText(null);
                    successDialog.setContentText("Deletion was successful!");
                    successDialog.showAndWait();
                }
            });
        });

        // Add buttons to bottom section
        buttonBox.getChildren().addAll(updateSaveButton, deleteButton);

        // Arrange sections in the root layout
        HBox topSection = new HBox(topLeftSection, detailsBox);
        topSection.setSpacing(20);

        root.setTop(topSection);
        root.setBottom(buttonBox);

        BorderPane.setMargin(topSection, new Insets(10));
        BorderPane.setMargin(buttonBox, new Insets(10));

        // Set Scene and Stage
        Scene scene = new Scene(root, 600, 300);  // Fixed size
        primaryStage.setTitle("Employee Card");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);  // Prevent resizing
        primaryStage.show();
        root.requestFocus();
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

    public static void main(String[] args) {
        launch(args);
    }
}
