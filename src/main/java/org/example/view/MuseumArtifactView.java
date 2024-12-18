package org.example.view;

import org.example.controller.MuseumArtifactController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MuseumArtifactView extends JFrame {
    private JTable artifactTable;
    private JTable updateArtifactTable;
    private JTextField updateNameField;
    private JTextField updateCategoryField;
    private JTextArea updateDescriptionArea;
    private JTextField updateAcquisitionDateField;
    private JTextField updateLocationField;
    private JButton addButton, updateButton, deleteButton, searchButton, searchByCategoryButton;
    private JTextField nameField, categoryField, descriptionField, locationField, acquisitionDateField;
    private JTextField searchField, searchCategoryField;
    private JTabbedPane tabbedPane;  // For switching between operations
    private DefaultTableModel tableModel;

    private JButton refreshUpdatePanelButton;

    // Panels for each operation
    private JPanel createArtifactPanel, getArtifactPanel, searchByCategoryPanel, updateArtifactPanel;

    // Labels for displaying artifact details after search (NEW)
    private JLabel idLabel, categoryLabel, descriptionLabel, locationLabel, acquisitionDateLabel;
    private JTextField idField, categoryFieldResult, descriptionFieldResult, locationFieldResult, acquisitionDateFieldResult;

    // Label for success/failure message
    private JLabel messageLabel;

    public MuseumArtifactView() {
        setTitle("Museum Management System - Artifact");
        setSize(600, 500); // Increased size for additional input fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize Panels for different operations
        createArtifactPanel = new JPanel(new BorderLayout());
        getArtifactPanel = new JPanel(new BorderLayout());
        searchByCategoryPanel = new JPanel(new BorderLayout());
        updateArtifactPanel = new JPanel();
        updateArtifactPanel.setLayout(new GridLayout(7, 2));

        // Add tabs for Create and Get Artifact operations
        tabbedPane.addTab("Create Artifact", createArtifactPanel);
        tabbedPane.addTab("Get Artifact", getArtifactPanel);
        tabbedPane.addTab("Search by Category", searchByCategoryPanel);
        tabbedPane.addTab("Update Artifact", updateArtifactPanel);


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

        // Set up Search by Category panel (NEW)
        JPanel categorySearchPanel = new JPanel(new FlowLayout());
        categorySearchPanel.add(new JLabel("Enter Category:"));
        searchCategoryField = new JTextField(20);
        categorySearchPanel.add(searchCategoryField);

        searchByCategoryButton = new JButton("Search by Category");
        categorySearchPanel.add(searchByCategoryButton);

        searchByCategoryPanel.add(categorySearchPanel, BorderLayout.NORTH);

        artifactTable = new JTable();
        searchByCategoryPanel.add(new JScrollPane(artifactTable), BorderLayout.CENTER);


        String[] columnNames = {"Name", "Category", "Description", "Acquisition Date", "Location"};
        tableModel = new DefaultTableModel(columnNames, 0);
        updateArtifactTable = new JTable(tableModel);
        updateArtifactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateArtifactTable.setCellSelectionEnabled(false);
        JScrollPane tableScrollPane = new JScrollPane(updateArtifactTable);
        updateArtifactPanel.add(tableScrollPane);


        updateNameField = new JTextField(20);
        updateCategoryField = new JTextField(20);
        updateDescriptionArea = new JTextArea(5, 20);
        updateAcquisitionDateField = new JTextField(20);
        updateLocationField = new JTextField(20);

        updateArtifactPanel.add(new JLabel("Name:"));
        updateArtifactPanel.add(updateNameField);
        updateArtifactPanel.add(new JLabel("Category:"));
        updateArtifactPanel.add(updateCategoryField);
        updateArtifactPanel.add(new JLabel("Description:"));
        updateArtifactPanel.add(new JScrollPane(updateDescriptionArea));
        updateArtifactPanel.add(new JLabel("Acquisition Date:"));
        updateArtifactPanel.add(updateAcquisitionDateField);
        updateArtifactPanel.add(new JLabel("Location:"));
        updateArtifactPanel.add(updateLocationField);

        // Update button
        updateButton = new JButton("Update");
        updateArtifactPanel.add(updateButton);
        refreshUpdatePanelButton = new JButton("Refresh");
        updateArtifactPanel.add(refreshUpdatePanelButton);

        //add(tabbedPane);
    }

    public void setUpdateTableData(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) updateArtifactTable.getModel();
        model.setRowCount(0); // Clear existing data
        for (Object[] row : data) {
            model.addRow(row);
        }
    }


    private void clearUpdateFields() {
        updateNameField.setText("");
        updateCategoryField.setText("");
        updateDescriptionArea.setText("");
        updateAcquisitionDateField.setText("");
        updateLocationField.setText("");
    }

    public JButton getRefreshButton() {
        return refreshUpdatePanelButton;
    }




    public JButton getSearchByCategoryButton() {
        return searchByCategoryButton; // NEW
    }

    public String getSearchCategoryFieldInput() {
        return searchCategoryField.getText(); // NEW
    }

    public JTable getArtifactTable() {
        return artifactTable;
    }
    public JTable getUpdateArtifactTable() {
        return updateArtifactTable;
    }

    public void setArtifactTableData(Object[][] data, String[] columnNames) {
        artifactTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public JButton getAddButton() {
        return addButton;
    }
    public JButton getUpdateButton(){
        return updateButton;
    }



    /*public Date getUpdateArtifactAcquisitionDateInput() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(updateAcquisitionDateField.getText()); // Parse the date from text
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }*/

    public JTextField getUpdateArtifactNameInput() {
        return updateNameField;
    }

    public JTextField getUpdateArtifactCategoryInput() {
        return updateCategoryField;
    }

    public JTextArea getUpdateArtifactDescriptionInput() {
        return updateDescriptionArea;
    }

    public JTextField getUpdateArtifactAcquisitionDateInput() {
        return updateAcquisitionDateField;
    }

    public JTextField getUpdateArtifactLocationInput() {
        return updateLocationField;
    }


    public String getUpdateArtifactNameText() {
        return updateNameField.getText();
    }

    public String getUpdateArtifactCategoryText() {
        return updateCategoryField.getText();
    }

    public String getUpdateArtifactDescriptionText() {
        return updateDescriptionArea.getText();
    }



    public String getUpdateArtifactLocationText() {
        return updateLocationField.getText();
    }

    public String getUpdateArtifactAcquisitionDateText() {
        // Get the selected row from the table
        int selectedRow = updateArtifactTable.getSelectedRow();

        // Check if a row is selected
        if (selectedRow == -1) {
            return ""; // No row selected, return empty string or error message
        }

        // Get the acquisition date from the table (column 3 for the date)
        String acquisitionDate = (String) updateArtifactTable.getValueAt(selectedRow, 3);

        // Check if the acquisition date is empty or null
        if (acquisitionDate != null && !acquisitionDate.trim().isEmpty()) {
            try {
                // Parse the date if it is not empty
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(acquisitionDate);
                // Return formatted date
                return new SimpleDateFormat("yyyy-MM-dd").format(date);
            } catch (ParseException ex) {
                ex.printStackTrace(); // Log the exception or handle it as needed
                // Optionally, you can return a default value or an empty string
                return ""; // or you could return a specific error message if desired
            }
        } else {
            // Handle the case where the acquisition date is empty (e.g., return an empty string or a default message)
            return ""; // or you could return a default date like "0000-00-00" or some other placeholder
        }
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

    public void clearUpdatePanelInputFields() {
        updateNameField.setText("");
        updateCategoryField.setText("");
        updateDescriptionArea.setText("");
        updateAcquisitionDateField.setText(""); // Clear the date field
        updateLocationField.setText("");
        //searchField.setText(""); // Clear the search field
    }

    /*public void setMessage(String message) {
        messageLabel.setText(message);
    }*/
    public void setMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(Color.RED); // Optional: change color for errors
        messageLabel.revalidate();
        messageLabel.repaint();
    }


    public void updateCategoryTable(List<String[]> tableData) {
        // Define column names for the table
        String[] columnNames = {"ID", "Name", "Category", "Description", "Acquisition Date", "Location"};

        // Convert List<String[]> to Object[][]
        Object[][] data = tableData.toArray(new Object[0][0]);

        // Update the table model with new data
        artifactTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public void clearCategoryTable() {
        // Set an empty table model to clear the table
        String[] columnNames = {"ID", "Name", "Category", "Description", "Acquisition Date", "Location"};
        artifactTable.setModel(new DefaultTableModel(new Object[0][0], columnNames));
    }

    public void clearSearchCategoryField() {
        searchCategoryField.setText("");
    }

    public void setArtifactTableDataNew(Object[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        artifactTable.setModel(model);

        // Enable row selection
        artifactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a listener to handle row selection
        artifactTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = artifactTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Retrieve data from selected row
                    String id = (String) artifactTable.getValueAt(selectedRow, 0); // Assuming the ID is in the first column
                    String name = (String) artifactTable.getValueAt(selectedRow, 1); // Assuming Name is in the second column
                    String category = (String) artifactTable.getValueAt(selectedRow, 2);
                    String description = (String) artifactTable.getValueAt(selectedRow, 3);
                    String acquisitionDate = (String) artifactTable.getValueAt(selectedRow, 4);
                    String location = (String) artifactTable.getValueAt(selectedRow, 5);

                    // Populate the fields for update
                    setArtifactDetails(id, category, description, location, acquisitionDate);
                }
            }
        });
    }

    public DefaultTableModel getTableModel(){
        return tableModel;
    }




}
