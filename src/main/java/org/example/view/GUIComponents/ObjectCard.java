package org.example.view.GUIComponents;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import org.example.controller.EmployeeController;
import org.example.entity.MuseumArtifact;
import org.example.view.GUIs.EmployeeGUI;

import java.sql.SQLException;

public class ObjectCard extends BorderPane {
    private Label objectNameLabel;
    private ImageView objectImageView;
    private TextField nameField, dateField, categoryField, locationInMuseumField;
    private TextArea infoDump;
    private Button updateSaveButton, deleteButton, backButton;
    private boolean isEditing;
    private EmployeeController controller;
    private MuseumArtifact museumArtifact;

    public ObjectCard(VBox previousPage, String userType, EmployeeGUI EmpGUI) throws SQLException {
        // Set padding and background
        controller = new EmployeeController(EmpGUI);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #d3d3d3;");

        // Top Section
        VBox topLeftSection = new VBox();
        topLeftSection.setSpacing(10);
        topLeftSection.setAlignment(Pos.TOP_LEFT);

        objectNameLabel = new Label("Object Name");
        objectNameLabel.setFont(Font.font("Arial", 18));
        objectNameLabel.setStyle("-fx-background-color: #c4a7a7; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
        objectNameLabel.setMaxWidth(Double.MAX_VALUE);
        objectNameLabel.setAlignment(Pos.CENTER_LEFT);

        objectImageView = new ImageView();
        objectImageView.setFitWidth(150);
        objectImageView.setFitHeight(150);
        objectImageView.setPreserveRatio(true);
        objectImageView.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c4c4c4; -fx-border-radius: 10; -fx-background-radius: 10;");

        topLeftSection.getChildren().addAll(objectNameLabel, objectImageView);

        // Center Section
        VBox detailsBox = new VBox(10);
        detailsBox.setAlignment(Pos.TOP_LEFT);

        nameField = createEditableField();
        dateField = createEditableField();
        categoryField = createEditableField();
        locationInMuseumField = createEditableField();

        detailsBox.getChildren().addAll(
                createLabeledField("Name: ", nameField),
                createLabeledField("Acquisition Date: ", dateField),
                createLabeledField("Category: ", categoryField),
                createLabeledField("Location in Museum: ", locationInMuseumField)
        );

        infoDump = new TextArea("Detailed info...");
        infoDump.setWrapText(true);
        infoDump.setEditable(false);
        infoDump.setPrefHeight(100);
        detailsBox.getChildren().add(infoDump);

        // Bottom Section
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        if(userType!="Guest"){
            updateSaveButton = new Button("Update Object");
            updateSaveButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
            updateSaveButton.setOnMouseEntered(e -> updateSaveButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
            updateSaveButton.setOnMouseExited(e -> updateSaveButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));

            deleteButton = new Button("Delete Object");
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
                    setEditable(categoryField, false);
                    setEditable(locationInMuseumField, false);
                    infoDump.setEditable(false);
                    infoDump.setStyle("-fx-control-inner-background: #ffeaea;");

                    // Save the updated details (Print to console for now)
                    System.out.println("Updated Name: " + nameField.getText());
                    System.out.println("Updated Date: " + dateField.getText());
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
                        ((BorderPane)this.getParent()).setCenter(previousPage);

                        controller.handleDeleteObject(museumArtifact);
                    }
                });
            });
        }else{
            updateSaveButton = new Button(); updateSaveButton.setVisible(false);
            deleteButton = new Button(); deleteButton.setVisible(false);
        }

        backButton = new Button("GO BACK");
        backButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 10;"));
        backButton.setOnAction(event -> ((BorderPane)this.getParent()).setCenter(previousPage));

        buttonBox.getChildren().addAll(updateSaveButton, deleteButton, backButton);

        // Add sections to layout
        this.setTop(topLeftSection);
        this.setCenter(detailsBox);
        this.setBottom(buttonBox);

        // Load the image asynchronously
        loadImageAsync("https://themeisle.com/blog/wp-content/uploads/2024/06/Online-Image-Optimizer-Test-Image-JPG-Version.jpeg");
    }

    private void loadImageAsync(String imageUrl) {
        new Thread(() -> {
            Image image = new Image(imageUrl);
            Platform.runLater(() -> objectImageView.setImage(image));
        }).start();
    }

    private TextField createEditableField() {
        TextField textField = new TextField();
        setEditable(textField, false);
        return textField;
    }
    private void setEditable(TextField field, boolean editable) {
        field.setEditable(editable);
        if (editable) {
            field.setStyle("-fx-control-inner-background: #ffffff; -fx-border-color: #c4c4c4;");
        } else {
            field.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        }
    }

    private HBox createLabeledField(String labelText, TextField textField) {
        Label label = new Label(labelText);
        HBox hBox = new HBox(10, label, textField);
        hBox.setAlignment(Pos.BASELINE_LEFT);
        return hBox;
    }

    public void updateCard(MuseumArtifact artifact) {
        this.museumArtifact = artifact;
        objectNameLabel.setText(artifact.getName());
        //objectImageView.setImage(new Image(artifact.getImageUrl(), 150, 150, true, true)); // Adjust as needed
        nameField.setText(artifact.getName());
        dateField.setText(String.valueOf(artifact.getAcquisitionDate()));
        categoryField.setText(artifact.getCategory());
        locationInMuseumField.setText(artifact.getLocationInMuseum());
        infoDump.setText(artifact.getDescription());
    }
}
