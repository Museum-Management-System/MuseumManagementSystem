package org.example.view.GUIs;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GuestUserGUI extends UserGUI {

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        primaryStage.setTitle("Guest User Interface");
        initGuestUserInterface();
    }
    private void initGuestUserInterface() {
        Label label = new Label("Welcome, Guest User!");
        StackPane stackPane = new StackPane(label);
        rootLayout.getChildren().add(stackPane);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
