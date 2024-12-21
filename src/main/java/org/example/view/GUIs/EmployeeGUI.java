package org.example.view.GUIs;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
        addObjectButton.setOnAction(e -> System.out.println("Add New Object button clicked!"));

        // Add this button to the objects page layout
        HBox topBox = (HBox) objectsPage.getChildren().get(3); // The top box with title
        topBox.getChildren().add(addObjectButton); // Add the button to the HBox
    }

    @Override
    protected String getUserType() { return "Employee";}
}
