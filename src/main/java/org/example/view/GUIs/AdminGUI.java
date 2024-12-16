package org.example.view.GUIs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Welcome, Admin User!");
        StackPane root = new StackPane(label);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Admin Interface");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
