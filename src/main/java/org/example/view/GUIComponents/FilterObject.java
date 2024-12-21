package org.example.view.GUIComponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FilterObject {
    private CheckBox[] categoryCheckboxes;
    private CheckBox[] locationCheckboxes;
    private TextField minDateField;
    private TextField maxDateField;
    private Button applyFilterButton;

    private ArrayList<String> categories;
    private ArrayList<String> locations;

    public FilterObject(ArrayList<String> categories, ArrayList<String> locations) {
        this.categories = categories;
        this.locations = locations;
    }

    public void show(Stage owner, Runnable onApply) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.setTitle("Filter Artifacts");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #D6CCE1;");

        // Category section
        Label categoryLabel = new Label("Category:");
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        GridPane categoryGrid = new GridPane();
        categoryGrid.setHgap(10);
        categoryGrid.setVgap(10);
        categoryGrid.setAlignment(Pos.CENTER_LEFT);
        categoryCheckboxes = new CheckBox[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            CheckBox checkBox = new CheckBox(categories.get(i));
            categoryCheckboxes[i] = checkBox;
            categoryGrid.add(checkBox, i % 4, i / 4); // Place checkbox in grid
        }

        // Acquisition date section
        Label dateLabel = new Label("Acquisition Date:");
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox dateBox = new HBox(10);
        minDateField = new TextField();
        minDateField.setPromptText("Min Date (YYYY-MM-DD)");
        maxDateField = new TextField();
        maxDateField.setPromptText("Max Date (YYYY-MM-DD)");
        dateBox.getChildren().addAll(minDateField, new Label("-"), maxDateField);

        // Location section
        Label locationLabel = new Label("Location in Museum:");
        locationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        GridPane locationGrid = new GridPane();
        locationGrid.setHgap(10);
        locationGrid.setVgap(10);
        locationGrid.setAlignment(Pos.CENTER_LEFT);
        locationCheckboxes = new CheckBox[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            CheckBox checkBox = new CheckBox(locations.get(i));
            locationCheckboxes[i] = checkBox;
            locationGrid.add(checkBox, i % 4, i / 4); // Place checkbox in grid
        }

        applyFilterButton = new Button("FILTER");
        applyFilterButton.setStyle("-fx-background-color: #ff9999; -fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
        applyFilterButton.setOnMouseEntered(event -> applyFilterButton.setStyle("-fx-background-color: #fb6767; -fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;"));
        applyFilterButton.setOnMouseExited(event -> applyFilterButton.setStyle("-fx-background-color: #ff9999; -fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;"));
        applyFilterButton.setOnAction(e -> {
            stage.close();
            if (onApply != null) onApply.run();
        });

        // Assemble layout
        root.getChildren().addAll(
                categoryLabel, categoryGrid,
                dateLabel, dateBox,
                locationLabel, locationGrid,
                applyFilterButton
        );

        Scene scene = new Scene(root, 500, 550);
        stage.setScene(scene);
        stage.show();
    }

    // Get selected filter options
    public ArrayList<String> getSelectedCategories() {
        ArrayList<String> selectedCategories = new ArrayList<>();
        for (CheckBox cb : categoryCheckboxes) {
            if (cb.isSelected()) {
                selectedCategories.add(cb.getText());
            }
        }
        return selectedCategories;
    }

    public ArrayList<String> getSelectedLocations() {
        ArrayList<String> selectedLocations = new ArrayList<>();
        for (CheckBox cb : locationCheckboxes) {
            if (cb.isSelected()) {
                selectedLocations.add(cb.getText());
            }
        }
        return selectedLocations;
    }

    public String getMinDate() {
        return minDateField.getText().trim();
    }

    public String getMaxDate() {
        return maxDateField.getText().trim();
    }
}
