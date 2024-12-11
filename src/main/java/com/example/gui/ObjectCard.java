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

public class ObjectCard extends Application {

    private boolean isEditing = false; // Flag to toggle between Update and Save

    @Override
    public void start(Stage primaryStage) {
        // Root Pane
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #d3d3d3;");

        // Top Section: Object Name and Image
        VBox topLeftSection = new VBox();
        topLeftSection.setSpacing(10);
        topLeftSection.setAlignment(Pos.TOP_LEFT);

        // Object Name
        Label objectNameLabel = new Label("Object Name");
        objectNameLabel.setFont(Font.font("Arial", 18));
        objectNameLabel.setStyle("-fx-background-color: #c4a7a7; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
        objectNameLabel.setMaxWidth(Double.MAX_VALUE);
        objectNameLabel.setAlignment(Pos.CENTER_LEFT);

        // Object Image
        ImageView objectImageView = new ImageView();
        objectImageView.setFitWidth(150);
        objectImageView.setFitHeight(150);
        objectImageView.setPreserveRatio(true);
        objectImageView.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c4c4c4; -fx-border-radius: 10; -fx-background-radius: 10;");
        objectImageView.setImage(new Image("https://via.placeholder.com/150"));

        // Add Name and Image to Top Section
        topLeftSection.getChildren().addAll(objectNameLabel, objectImageView);

        // Right Section: Editable Object Details
        VBox detailsBox = new VBox();
        detailsBox.setAlignment(Pos.TOP_LEFT);

        // Editable fields
        Label nameLabel = new Label("Name: ");
        TextField nameField = createEditableField("Example Object");

        Label dateLabel = new Label("Acquisition Date: ");
        TextField dateField = createEditableField("12th Dec 2023");

        Label fromWhereLabel = new Label("From Where: ");
        TextField fromWhereField = createEditableField("Example Location");

        Label categoryLabel = new Label("Category: ");
        TextField categoryField = createEditableField("Example Category");

        Label locationInMuseumLabel = new Label("Location in the museum: ");
        TextField locationInMuseumField = createEditableField("Example location");

        // Arrange Labels and TextFields Horizontally
        HBox nameBox = new HBox(nameLabel, nameField);
        HBox dateBox = new HBox(dateLabel, dateField);
        HBox fromWhereBox = new HBox(fromWhereLabel, fromWhereField);
        HBox categoryBox = new HBox(categoryLabel, categoryField);
        HBox locationBox = new HBox(locationInMuseumLabel, locationInMuseumField);

        // Set alignment to left for each HBox to align the beginnings
        nameBox.setAlignment(Pos.BASELINE_LEFT);
        dateBox.setAlignment(Pos.BASELINE_LEFT);
        fromWhereBox.setAlignment(Pos.BASELINE_LEFT);
        categoryBox.setAlignment(Pos.BASELINE_LEFT);
        locationBox.setAlignment(Pos.BASELINE_LEFT);

        // Use translateY to move the HBoxes slightly up
        nameBox.setTranslateY(-2);  // Moves up by 2 pixels
        dateBox.setTranslateY(-2);  // Moves up by 2 pixels
        fromWhereBox.setTranslateY(-2);  // Moves up by 2 pixels
        categoryBox.setTranslateY(-2);
        locationBox.setTranslateY(-2);

        detailsBox.getChildren().addAll(nameBox, dateBox, fromWhereBox, categoryBox, locationBox);
        detailsBox.setTranslateY(40); // Adjust the value (positive to lower)

        // Center Section: Detailed Info
        TextArea infoDump = new TextArea("More detailed info. Maybe a story about the object or more.");
        infoDump.setWrapText(true);
        infoDump.setPrefHeight(100);
        infoDump.setEditable(false);
        infoDump.setStyle("-fx-control-inner-background: #ffeaea; -fx-border-color: #c4c4c4; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Bottom Section: Buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        Button updateSaveButton = new Button("Update Object");
        updateSaveButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        updateSaveButton.setOnMouseEntered(e -> updateSaveButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        updateSaveButton.setOnMouseExited(e -> updateSaveButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));

        Button deleteButton = new Button("Delete Object");
        deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));


        // Button Actions
        updateSaveButton.setOnAction(event -> {
            if (!isEditing) {
                // Enter editing mode
                isEditing = true;
                updateSaveButton.setText("Save Object");

                // Enable editing for all fields
                setEditable(nameField, true);
                setEditable(dateField, true);
                setEditable(fromWhereField, true);
                setEditable(categoryField, true);
                setEditable(locationInMuseumField, true);
                infoDump.setEditable(true);
                infoDump.setStyle("-fx-control-inner-background: #ffffff;");
            } else {
                // Save and exit editing mode
                isEditing = false;
                updateSaveButton.setText("Update Object");

                // Disable editing for all fields
                setEditable(nameField, false);
                setEditable(dateField, false);
                setEditable(fromWhereField, false);
                setEditable(categoryField, false);
                setEditable(locationInMuseumField, false);
                infoDump.setEditable(false);
                infoDump.setStyle("-fx-control-inner-background: #ffeaea;");

                // Save the updated details (Print to console for now)
                System.out.println("Updated Name: " + nameField.getText());
                System.out.println("Updated Date: " + dateField.getText());
                System.out.println("Updated From Where: " + fromWhereField.getText());
                System.out.println("Updated Category: " + categoryField.getText());
                System.out.println("Updated Location in the Museum: " + locationInMuseumField.getText());
                System.out.println("Updated Info: " + infoDump.getText());
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
        root.setCenter(infoDump);
        root.setBottom(buttonBox);

        BorderPane.setMargin(topSection, new Insets(10));
        BorderPane.setMargin(infoDump, new Insets(10));
        BorderPane.setMargin(buttonBox, new Insets(10));

        // Set Scene and Stage
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Object Card");
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
