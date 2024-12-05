package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MuseumArtifactView extends JFrame {
    private JTable artifactTable;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField nameField, categoryField, descriptionField, locationField, acquisitionDateField;
    private JTextField searchField;
    private JTabbedPane tabbedPane;  // For switching between operations

    // Panels for each operation
    private JPanel createArtifactPanel, getArtifactPanel;

    // Labels for displaying artifact details after search (NEW)
    private JLabel idLabel, categoryLabel, descriptionLabel, locationLabel, acquisitionDateLabel;
    private JTextField idField, categoryFieldResult, descriptionFieldResult, locationFieldResult, acquisitionDateFieldResult;

    // Label for success/failure message
    private JLabel messageLabel;

    public MuseumArtifactView() {
        setTitle("Museum Management System - Employee");
        setSize(600, 500); // Increased size for additional input fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize Panels for different operations
        createArtifactPanel = new JPanel(new BorderLayout());
        getArtifactPanel = new JPanel(new BorderLayout());

        // Add tabs for Create and Get Artifact operations
        tabbedPane.addTab("Create Artifact", createArtifactPanel);
        tabbedPane.addTab("Get Artifact", getArtifactPanel);

        // Add the tabbed pane to the main frame
        add(tabbedPane, BorderLayout.CENTER);

        // Set up Create Artifact panel
        JPanel createInputPanel = new JPanel(new GridLayout(7, 2)); // Added one more row for the message label
        createInputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        createInputPanel.add(nameField);

        createInputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        createInputPanel.add(categoryField);

        createInputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        createInputPanel.add(descriptionField);

        createInputPanel.add(new JLabel("Acquisition Date (YYYY-MM-DD):"));
        acquisitionDateField = new JTextField();
        createInputPanel.add(acquisitionDateField);

        createInputPanel.add(new JLabel("Location in Museum:"));
        locationField = new JTextField();
        createInputPanel.add(locationField);

        addButton = new JButton("Add Artifact");
        createInputPanel.add(addButton);

        // Add the success message label (hidden initially)
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.GREEN);
        createInputPanel.add(messageLabel);

        createArtifactPanel.add(createInputPanel, BorderLayout.CENTER);

        // Set up Get Artifact panel
        JPanel getInputPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Artifact Name:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search Artifact");
        searchPanel.add(searchButton);

        getInputPanel.add(searchPanel, BorderLayout.NORTH);

        getArtifactPanel.add(getInputPanel, BorderLayout.NORTH);

        // Panel for displaying artifact details after search (NEW)
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2)); // Grid for displaying details

        // ID field
        idLabel = new JLabel("ID: ");
        detailsPanel.add(idLabel);
        idField = new JTextField();
        idField.setEditable(false);  // Make the ID field non-editable
        detailsPanel.add(idField);

        // Category field
        categoryLabel = new JLabel("Category: ");
        detailsPanel.add(categoryLabel);
        categoryFieldResult = new JTextField();
        categoryFieldResult.setEditable(false);
        detailsPanel.add(categoryFieldResult);

        // Description field
        descriptionLabel = new JLabel("Description: ");
        detailsPanel.add(descriptionLabel);
        descriptionFieldResult = new JTextField();
        descriptionFieldResult.setEditable(false);
        detailsPanel.add(descriptionFieldResult);

        // Location field
        locationLabel = new JLabel("Location in Museum: ");
        detailsPanel.add(locationLabel);
        locationFieldResult = new JTextField();
        locationFieldResult.setEditable(false);
        detailsPanel.add(locationFieldResult);

        // Acquisition Date field
        acquisitionDateLabel = new JLabel("Acquisition Date: ");
        detailsPanel.add(acquisitionDateLabel);
        acquisitionDateFieldResult = new JTextField();
        acquisitionDateFieldResult.setEditable(false);
        detailsPanel.add(acquisitionDateFieldResult);

        getArtifactPanel.add(detailsPanel, BorderLayout.CENTER);  // Added to display artifact info

        // Table to display artifact details if needed
        artifactTable = new JTable();
        getArtifactPanel.add(new JScrollPane(artifactTable), BorderLayout.SOUTH);
    }

    public JTable getArtifactTable() {
        return artifactTable;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public String getArtifactNameInput() {
        return nameField.getText();
    }

    public String getArtifactCategoryInput() {
        return categoryField.getText();
    }

    public String getArtifactDescriptionInput() {
        return descriptionField.getText();
    }

    public Date getArtifactAcquisitionDateInput() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(acquisitionDateField.getText()); // Parse the date from text
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }

    public String getArtifactLocationInput() {
        return locationField.getText();
    }

    public String getSearchFieldInput() {
        return searchField.getText();
    }

    // Method to update the artifact details in the UI after a search (NEW)
    public void setArtifactDetails(String id, String category, String description, String location, String acquisitionDate) {
        idField.setText(id);
        categoryFieldResult.setText(category);
        descriptionFieldResult.setText(description);
        locationFieldResult.setText(location);
        acquisitionDateFieldResult.setText(acquisitionDate);
    }

    public void clearInputFields() {
        nameField.setText("");
        categoryField.setText("");
        descriptionField.setText("");
        acquisitionDateField.setText(""); // Clear the date field
        locationField.setText("");
        searchField.setText(""); // Clear the search field
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
