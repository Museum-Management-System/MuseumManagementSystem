package org.example.view;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//There might be some small problems will be checked.
public class EmployeeView extends JFrame {
    private JTable employeeTable;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField nameField, surNameField, passwordField, entranceDateField;
    private JTextField searchField;
    private JTabbedPane tabbedPane;

    // Panels for each operation
    private JPanel createEmployeePanel, getEmployeePanel, searchByIDPanel;

    // Labels for displaying employee details after search
    private JLabel idLabel, nameLabel, surnameLabel, entranceDateLabel;
    private JTextField idField, nameFieldResult, surnameFieldResult, entranceDateFieldResult;

    // Label for success/failure message
    private JLabel messageLabel;

    public EmployeeView() {
        setTitle("Museum Management System - Employee");
        setSize(600, 500); // Adjusted size for the content
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize Panels for different operations
        createEmployeePanel = new JPanel(new BorderLayout());
        getEmployeePanel = new JPanel(new BorderLayout());
        searchByIDPanel = new JPanel(new BorderLayout());

        // Add tabs for Create and Search Employee operations
        tabbedPane.addTab("Add Employee", createEmployeePanel);
        tabbedPane.addTab("Get Employee", getEmployeePanel);
        tabbedPane.addTab("Search by Employee ID", searchByIDPanel);

        // Add the tabbed pane to the main frame
        add(tabbedPane, BorderLayout.CENTER);

        // Set up Add Employee panel
        JPanel createInputPanel = new JPanel(new GridLayout(5, 2)); // Layout for input fields and message
        createInputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        createInputPanel.add(nameField);

        createInputPanel.add(new JLabel("Surname:"));
        surNameField = new JTextField();
        createInputPanel.add(surNameField);

        createInputPanel.add(new JLabel("Password:"));
        passwordField = new JTextField();
        createInputPanel.add(passwordField);

        createInputPanel.add(new JLabel("Entrance Date (YYYY-MM-DD):"));
        entranceDateField = new JTextField();
        createInputPanel.add(entranceDateField);

        addButton = new JButton("Add Employee");
        createInputPanel.add(addButton);

        // Add the success message label (hidden initially)
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.GREEN);
        createInputPanel.add(messageLabel);

        createEmployeePanel.add(createInputPanel, BorderLayout.CENTER);

        // Set up Get Employee panel
        JPanel getInputPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Employee Name:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search Employee");
        searchPanel.add(searchButton);

        getInputPanel.add(searchPanel, BorderLayout.NORTH);

        getEmployeePanel.add(getInputPanel, BorderLayout.NORTH);

        // Panel for displaying employee details after search
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2)); // Grid for displaying details

        // ID field
        idLabel = new JLabel("ID: ");
        detailsPanel.add(idLabel);
        idField = new JTextField();
        idField.setEditable(false); // Make the ID field non-editable
        detailsPanel.add(idField);

        // Name field
        nameLabel = new JLabel("Name: ");
        detailsPanel.add(nameLabel);
        nameFieldResult = new JTextField();
        nameFieldResult.setEditable(false);
        detailsPanel.add(nameFieldResult);

        // Surname field
        surnameLabel = new JLabel("Surname: ");
        detailsPanel.add(surnameLabel);
        surnameFieldResult = new JTextField();
        surnameFieldResult.setEditable(false);
        detailsPanel.add(surnameFieldResult);

        // Entrance Date field
        entranceDateLabel = new JLabel("Entrance Date: ");
        detailsPanel.add(entranceDateLabel);
        entranceDateFieldResult = new JTextField();
        entranceDateFieldResult.setEditable(false);
        detailsPanel.add(entranceDateFieldResult);

        getEmployeePanel.add(detailsPanel, BorderLayout.CENTER);

        // Table to display employee details if needed
        employeeTable = new JTable();
        getEmployeePanel.add(new JScrollPane(employeeTable), BorderLayout.SOUTH);

        // Set up Search by Employee ID panel
        JPanel idSearchPanel = new JPanel(new FlowLayout());
        idSearchPanel.add(new JLabel("Enter Employee ID:"));
        entranceDateField = new JTextField(20);
        idSearchPanel.add(entranceDateField);

        searchByIDPanel.add(idSearchPanel, BorderLayout.NORTH);

        employeeTable = new JTable();
        searchByIDPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
    }

    public String getSearchEmployeeIDInput() {
        return entranceDateField.getText();
    }

    public JTable getEmployeeTable() {
        return employeeTable;
    }

    public void setEmployeeTableData(Object[][] data, String[] columnNames) {
        employeeTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public String getEmployeeNameInput() {
        return nameField.getText();
    }

    public String getEmployeeSurnameInput() {
        return surNameField.getText();
    }

    public String getEmployeePasswordInput() {
        return passwordField.getText();
    }

    public Date getEmployeeEntranceDateInput() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(entranceDateField.getText()); // Parse the date from text
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }

    public String getSearchFieldInput() {
        return searchField.getText();
    }

    public void setEmployeeDetails(String id, String name, String surname, String entranceDate) {
        idField.setText(id);
        nameFieldResult.setText(name);
        surnameFieldResult.setText(surname);
        entranceDateFieldResult.setText(entranceDate);
    }

    public void clearInputFields() {
        nameField.setText("");
        surNameField.setText("");
        passwordField.setText("");
        entranceDateField.setText("");
        searchField.setText("");
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void updateEmployeeTable(List<String[]> tableData) {
        String[] columnNames = {"ID", "Name", "Surname", "Password", "Entrance Date"};

        Object[][] data = tableData.toArray(new Object[0][0]);

        employeeTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
