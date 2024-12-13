package com.example.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// Non-editable ObjectCard for Guest users

public class ObjectCardNonEditable extends Application {

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

        // Right Section: Object Details
        VBox detailsBox = new VBox();
        detailsBox.setAlignment(Pos.TOP_LEFT);

        // Non-editable fields (using TextField and setting editable to false)
        Label nameLabel = new Label("Name: ");
        TextField nameField = new TextField("Example Object");
        nameField.setEditable(false);  // Make it non-editable
        nameField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");

        Label dateLabel = new Label("Acquisition Date: ");
        TextField dateField = new TextField("12th Dec 2023");
        dateField.setEditable(false);  // Make it non-editable
        dateField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");

        Label fromWhereLabel = new Label("From Where: ");
        TextField fromWhereField = new TextField("Example Location");
        fromWhereField.setEditable(false);  // Make it non-editable
        fromWhereField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");

        Label categoryLabel = new Label("Category: ");
        TextField categoryField = new TextField("Example Category");
        categoryField.setEditable(false);  // Make it non-editable
        categoryField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");

        Label locationInMuseumLabel = new Label("Location in the museum: ");
        TextField locationInMuseumField = new TextField("Example location");
        locationInMuseumField.setEditable(false);  // Make it non-editable
        locationInMuseumField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");

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
        infoDump.setEditable(false);  // Make the info text area non-editable
        infoDump.setStyle("-fx-control-inner-background: #ffeaea; -fx-border-color: #c4c4c4; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Bottom Section: Buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

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

    public static void main(String[] args) {
        launch(args);
    }
}
