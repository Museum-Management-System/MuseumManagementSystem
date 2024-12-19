package org.example.view.GUIs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserGUI extends Application {
    protected Stage primaryStage;
    protected VBox rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("User Interface");

        Button objectsButton = new Button("  OBJECTS   ");
        objectsButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-shape: 'M0,0 L1,0 L1,1 L0,1 Z';");

        Button employeesButton = new Button("EMPLOYEES");
        employeesButton.setStyle("-fx-background-color: #c192cf; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-shape: 'M0,0 L1,0 L1,1 L0,1 Z';");

        VBox sidebar = new VBox(objectsButton, employeesButton);
        sidebar.setSpacing(15); // Set spacing between buttons
        sidebar.setStyle("-fx-background-color: #909090; -fx-padding: 50px 15px 50px 15px;");

        sidebar.setMaxHeight(Double.MAX_VALUE);
        sidebar.setStyle("-fx-background-color: #909090; -fx-padding: 50px 15px 50px 15px; -fx-alignment: center-left;");

        Label welcomeLabel = new Label("Welcome");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(welcomeLabel);
        root.setStyle("-fx-background-color: #d6ced6;");

        objectsButton.setOnAction(e -> System.out.println("Objects button clicked"));
        employeesButton.setOnAction(e -> System.out.println("Employees button clicked"));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Museum Management System");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
