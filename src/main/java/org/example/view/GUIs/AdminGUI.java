package org.example.view.GUIs;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminGUI extends EmployeeGUI {

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        primaryStage.setTitle("Guest User Interface");
        initAdminInterface();
    }

    private void initAdminInterface() {
        Label label = new Label("Welcome, Guest User!");
        StackPane stackPane = new StackPane(label);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
