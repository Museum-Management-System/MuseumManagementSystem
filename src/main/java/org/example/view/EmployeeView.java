package org.example.view;
//If a problem exists let me know.
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeeView extends JFrame {
    private JTable employeeTable;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField IDField ,nameField, emailField, phoneNumField, jobTitleField, sectionNameField, roleField;
    private JTextField searchField;
    private JTabbedPane tabbedPane;

    private JPanel createEmployeePanel, getEmployeePanel, searchByIDPanel;

    private JLabel idLabel, nameLabel, emailLabel, phoneNumLabel, jobTitleLabel, sectionNameLabel, roleLabel;
    private JTextField idField, nameFieldResult, emailFieldResult, phoneNumFieldResult, jobTitleFieldResult, sectionNameFieldResult, roleFieldResult, entranceDateFieldResult;

    private JLabel messageLabel;

    public EmployeeView() {
        setTitle("Museum Management System - Employee");
        setSize(700, 600); // Adjusted size for the content
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

        // Create Employee Panel
        JPanel createInputPanel = new JPanel(new GridLayout(9, 2));

        createInputPanel.add(new JLabel("ID:"));
        IDField = new JTextField();
        createInputPanel.add(IDField);


        createInputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        createInputPanel.add(nameField);

        createInputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        createInputPanel.add(emailField);

        createInputPanel.add(new JLabel("Phone Number:"));
        phoneNumField = new JTextField();
        createInputPanel.add(phoneNumField);

        createInputPanel.add(new JLabel("Job Title:"));
        jobTitleField = new JTextField();
        createInputPanel.add(jobTitleField);

        createInputPanel.add(new JLabel("Section Name:"));
        sectionNameField = new JTextField();
        createInputPanel.add(sectionNameField);

        createInputPanel.add(new JLabel("Role:"));
        roleField = new JTextField();
        createInputPanel.add(roleField);


        addButton = new JButton("Add Employee");
        createInputPanel.add(addButton);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.GREEN);
        createInputPanel.add(messageLabel);

        createEmployeePanel.add(createInputPanel, BorderLayout.CENTER);

        // Get Employee Panel
        JPanel getInputPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Employee Name:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search Employee");
        searchPanel.add(searchButton);

        getInputPanel.add(searchPanel, BorderLayout.NORTH);

        getEmployeePanel.add(getInputPanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(8, 2));

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

        emailLabel = new JLabel("Email: ");
        detailsPanel.add(emailLabel);
        emailFieldResult = new JTextField();
        emailFieldResult.setEditable(false);
        detailsPanel.add(emailFieldResult);

        phoneNumLabel = new JLabel("Phone Number: ");
        detailsPanel.add(phoneNumLabel);
        phoneNumFieldResult = new JTextField();
        phoneNumFieldResult.setEditable(false);
        detailsPanel.add(phoneNumFieldResult);

        jobTitleLabel = new JLabel("Job Title: ");
        detailsPanel.add(jobTitleLabel);
        jobTitleFieldResult = new JTextField();
        jobTitleFieldResult.setEditable(false);
        detailsPanel.add(jobTitleFieldResult);

        sectionNameLabel = new JLabel("Section Name: ");
        detailsPanel.add(sectionNameLabel);
        sectionNameFieldResult = new JTextField();
        sectionNameFieldResult.setEditable(false);
        detailsPanel.add(sectionNameFieldResult);

        roleLabel = new JLabel("Role: ");
        detailsPanel.add(roleLabel);
        roleFieldResult = new JTextField();
        roleFieldResult.setEditable(false);
        detailsPanel.add(roleFieldResult);

        getEmployeePanel.add(detailsPanel, BorderLayout.CENTER);

        employeeTable = new JTable();
        getEmployeePanel.add(new JScrollPane(employeeTable), BorderLayout.SOUTH);

        // Search by ID Panel
        JPanel idSearchPanel = new JPanel(new FlowLayout());
        idSearchPanel.add(new JLabel("Enter Employee ID:"));
        JTextField searchByIDField = new JTextField(20);
        idSearchPanel.add(searchByIDField);

        searchByIDPanel.add(idSearchPanel, BorderLayout.NORTH);

        employeeTable = new JTable();
        searchByIDPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
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

    public String getEmployeeEmailInput() {
        return emailField.getText();
    }

    public String getEmployeePhoneNumInput() {
        return phoneNumField.getText();
    }

    public String getEmployeeJobTitleInput() {
        return jobTitleField.getText();
    }

    public String getEmployeeSectionNameInput() {
        return sectionNameField.getText();
    }

    public String getEmployeeRoleInput() {
        return roleField.getText();
    }

    public void setEmployeeDetails(String id, String name, String email, String phoneNum, String jobTitle, String sectionName, String role) {
        idField.setText(id);
        nameFieldResult.setText(name);
        emailFieldResult.setText(email);
        phoneNumFieldResult.setText(phoneNum);
        jobTitleFieldResult.setText(jobTitle);
        sectionNameFieldResult.setText(sectionName);
        roleFieldResult.setText(role);
    }

    public void clearInputFields() {
        nameField.setText("");
        emailField.setText("");
        phoneNumField.setText("");
        jobTitleField.setText("");
        sectionNameField.setText("");
        roleField.setText("");
        searchField.setText("");
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void updateEmployeeTable(List<String[]> tableData) {
        String[] columnNames = {"ID", "Name", "Email", "Phone Number", "Job Title", "Section Name", "Role", "Entrance Date"};

        Object[][] data = tableData.toArray(new Object[0][0]);

        employeeTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}