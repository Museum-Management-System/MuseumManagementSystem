package org.example.view.GUIs;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class EmployeeGUI extends GuestUserGUI {

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        primaryStage.setTitle("Employee Interface");
        initEmployeeInterface();
    }

    private void initEmployeeInterface() {
        Label label = new Label("Welcome, Employee User!");
        StackPane stackPane = new StackPane(label);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
