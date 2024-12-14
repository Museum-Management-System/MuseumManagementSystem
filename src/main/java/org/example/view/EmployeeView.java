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

    private JPanel createEmployeePanel, getEmployeePanel, searchByIDPanel;

    private JLabel idLabel, nameLabel, surnameLabel, entranceDateLabel;
    private JTextField idField, nameFieldResult, surnameFieldResult, entranceDateFieldResult;

    private JLabel messageLabel;

    public EmployeeView() {
        setTitle("Museum Management System - Employee");
        setSize(600, 500); // Adjusted size for the content
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        createEmployeePanel = new JPanel(new BorderLayout());
        getEmployeePanel = new JPanel(new BorderLayout());
        searchByIDPanel = new JPanel(new BorderLayout());

        tabbedPane.addTab("Add Employee", createEmployeePanel);
        tabbedPane.addTab("Get Employee", getEmployeePanel);
        tabbedPane.addTab("Search by Employee ID", searchByIDPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel createInputPanel = new JPanel(new GridLayout(5, 2));
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

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.GREEN);
        createInputPanel.add(messageLabel);

        createEmployeePanel.add(createInputPanel, BorderLayout.CENTER);

        JPanel getInputPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Employee Name:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search Employee");
        searchPanel.add(searchButton);

        getInputPanel.add(searchPanel, BorderLayout.NORTH);

        getEmployeePanel.add(getInputPanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(4, 2));

        idLabel = new JLabel("ID: ");
        detailsPanel.add(idLabel);
        idField = new JTextField();
        idField.setEditable(false);
        detailsPanel.add(idField);

        nameLabel = new JLabel("Name: ");
        detailsPanel.add(nameLabel);
        nameFieldResult = new JTextField();
        nameFieldResult.setEditable(false);
        detailsPanel.add(nameFieldResult);

        surnameLabel = new JLabel("Surname: ");
        detailsPanel.add(surnameLabel);
        surnameFieldResult = new JTextField();
        surnameFieldResult.setEditable(false);
        detailsPanel.add(surnameFieldResult);

        entranceDateLabel = new JLabel("Entrance Date: ");
        detailsPanel.add(entranceDateLabel);
        entranceDateFieldResult = new JTextField();
        entranceDateFieldResult.setEditable(false);
        detailsPanel.add(entranceDateFieldResult);

        getEmployeePanel.add(detailsPanel, BorderLayout.CENTER);

        employeeTable = new JTable();
        getEmployeePanel.add(new JScrollPane(employeeTable), BorderLayout.SOUTH);

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
