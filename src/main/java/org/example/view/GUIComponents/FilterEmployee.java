package org.example.view.GUIComponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FilterEmployee {
    private CheckBox[] jobTitleCheckboxes;
    private CheckBox[] sectionCheckboxes;
    private Button applyFilterButton;

    private ArrayList<String> jobTitles;
    private ArrayList<String> sections;

    public FilterEmployee(ArrayList<String> jobTitles, ArrayList<String> sections) {
        this.jobTitles = jobTitles;
        this.sections = sections;
    }

    public void show(Stage owner, Runnable onApply) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.setTitle("Filter Employees");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #D6CCE1;");

        // Job Title section
        Label jobTitleLabel = new Label("Job Title:");
        jobTitleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        GridPane jobTitleGrid = new GridPane();
        jobTitleGrid.setHgap(10);
        jobTitleGrid.setVgap(10);
        jobTitleGrid.setAlignment(Pos.CENTER_LEFT);
        jobTitleCheckboxes = new CheckBox[jobTitles.size()];
        for (int i = 0; i < jobTitles.size(); i++) {
            CheckBox checkBox = new CheckBox(jobTitles.get(i));
            jobTitleCheckboxes[i] = checkBox;
            jobTitleGrid.add(checkBox, i % 3, i / 3); // Place checkboxes in grid
        }

        Label sectionLabel = new Label("Section:");
        sectionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        GridPane sectionGrid = new GridPane();
        sectionGrid.setHgap(10);
        sectionGrid.setVgap(10);
        sectionGrid.setAlignment(Pos.CENTER_LEFT);
        sectionCheckboxes = new CheckBox[sections.size()];
        for (int i = 0; i < sections.size(); i++) {
            CheckBox checkBox = new CheckBox(sections.get(i));
            sectionCheckboxes[i] = checkBox;
            sectionGrid.add(checkBox, i % 3, i / 3); // Place checkboxes in grid
        }

        applyFilterButton = new Button("Apply Filter");
        applyFilterButton.setStyle("-fx-background-color: #ff9999; -fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
        applyFilterButton.setOnMouseEntered(event -> applyFilterButton.setStyle("-fx-background-color: #fb6767; -fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;"));
        applyFilterButton.setOnMouseExited(event -> applyFilterButton.setStyle("-fx-background-color: #ff9999; -fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;"));

        applyFilterButton.setOnAction(e -> {
            stage.close();
            if (onApply != null) onApply.run();
        });

        // Assemble layout
        root.getChildren().addAll(
                jobTitleLabel, jobTitleGrid,
                sectionLabel, sectionGrid,
                applyFilterButton
        );

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    public ArrayList<String> getSelectedJobTitles() {
        ArrayList<String> selectedJobTitles = new ArrayList<>();
        for (CheckBox cb : jobTitleCheckboxes) {
            if (cb.isSelected()) {
                selectedJobTitles.add(cb.getText());
            }
        }
        return selectedJobTitles;
    }

    public ArrayList<String> getSelectedSections() {
        ArrayList<String> selectedSections = new ArrayList<>();
        for (CheckBox cb : sectionCheckboxes) {
            if (cb.isSelected()) {
                selectedSections.add(cb.getText());
            }
        }
        return selectedSections;
    }
}