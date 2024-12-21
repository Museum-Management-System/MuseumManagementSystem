package org.example.view.GUIs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.entity.MuseumArtifact;

import java.sql.SQLException;

public class EmployeeGUI extends GuestUserGUI {

    @Override
    protected void openObjectsPage(Stage primaryStage) throws SQLException {
        // Call the base method to reuse the base layout
        super.openObjectsPage(primaryStage);

        // Access the root and objects page
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        VBox objectsPage = (VBox) root.getCenter();

        Button addObjectButton = new Button("Add New Object");
        addObjectButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white;");
        addObjectButton.setOnMouseEntered(event -> addObjectButton.setStyle("-fx-background-color: #b75cd3; -fx-text-fill: white;"));
        addObjectButton.setOnMouseExited(event -> addObjectButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white;"));
        addObjectButton.setOnAction(e -> displayEmptyObjectCard(root));

        // Add this button to the objects page layout
        HBox buttonsBox = (HBox) objectsPage.getChildren().get(2); // The top box with title
        buttonsBox.getChildren().add(addObjectButton); // Add the button to the HBox
    }
    protected void displayEmptyObjectCard(BorderPane root) {
        objectCard.updateCard();
        root.setCenter(objectCard);
    }
    @Override
    protected String getUserType() { return "Employee";}
}
