package org.example.view.GUIs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserGUI extends Application {
    protected Stage primaryStage;
    protected VBox rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("User Interface");

        initRootLayout();
    }

    private void initRootLayout() {
        rootLayout = new VBox();
        Scene scene = new Scene(rootLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
