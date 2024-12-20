package org.example.view.GUIComponents;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Filter {
    private CheckBox[] categoryCheckboxes;
    private CheckBox[] locationCheckboxes;
    private TextField minDateField;
    private TextField maxDateField;
    private Button applyFilterButton;

    private ArrayList<String> categories;
    private ArrayList<String> locations;

    public Filter(ArrayList<String> categories, ArrayList<String> locations) {
        this.categories = categories;
        this.locations = locations;
    }

    public void show(Stage owner, Runnable onApply) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.setTitle("Filter Artifacts");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Category section
        Label categoryLabel = new Label("Category:");
        VBox categoryBox = new VBox(5);
        categoryCheckboxes = new CheckBox[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryCheckboxes[i] = new CheckBox(categories.get(i));
            categoryBox.getChildren().add(categoryCheckboxes[i]);
        }

        // Acquisition date section
        Label dateLabel = new Label("Acquisition Date:");
        HBox dateBox = new HBox(10);
        minDateField = new TextField();
        minDateField.setPromptText("Min Date (YYYY-MM-DD)");
        maxDateField = new TextField();
        maxDateField.setPromptText("Max Date (YYYY-MM-DD)");
        dateBox.getChildren().addAll(minDateField, maxDateField);

        // Location section
        Label locationLabel = new Label("Location in Museum:");
        VBox locationBox = new VBox(5);
        locationCheckboxes = new CheckBox[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locationCheckboxes[i] = new CheckBox(locations.get(i));
            locationBox.getChildren().add(locationCheckboxes[i]);
        }

        // Apply filter button
        applyFilterButton = new Button("Apply Filter");
        applyFilterButton.setOnAction(e -> {
            stage.close();
            if (onApply != null) onApply.run();
        });

        root.getChildren().addAll(categoryLabel, categoryBox, dateLabel, dateBox, locationLabel, locationBox, applyFilterButton);

        Scene scene = new Scene(root, 400, 600);
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
