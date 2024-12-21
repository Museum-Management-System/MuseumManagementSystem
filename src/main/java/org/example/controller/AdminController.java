package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.dao.AdministratorDAO;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.Employee;
import org.example.entity.MuseumArtifact;
import org.example.service.DatabaseConnection;
import org.example.service.EmployeeService;
import org.example.view.GUIs.AdminGUI;
import org.example.view.GUIComponents.FilterEmployee;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.stage.Stage;

public class AdminController extends EmployeeController{

    private AdministratorDAO administratorDAO;
    private EmployeeService employeeService;
    private AdminGUI adminGUI;
    private Connection connection;
    protected Stage PrimaryStage;

    public AdminController(AdminGUI adminGUI) throws SQLException {
        super(adminGUI);
        this.adminGUI = adminGUI;
        this.connection = DatabaseConnection.getConnection();
        this.administratorDAO = new AdministratorDAO(connection);
        this.employeeService = new EmployeeService(administratorDAO);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void populateEmployeeList() {
        ArrayList<Employee> allEmployees = administratorDAO.searchEmployees();
        adminGUI.getEmployeeTableView().getItems().clear();
        adminGUI.getEmployeeTableView().getItems().addAll(allEmployees);
    }
    public void handleSearch() {
        TextField searchField = (TextField) adminGUI.getEmployeeTableView().getScene().lookup("#searchField");
        if (searchField != null) {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                System.out.println("Searching for employee with name: " + searchText);
                ArrayList<Employee> searchResults = administratorDAO.searchEmployees(searchText);
                adminGUI.getEmployeeTableView().getItems().clear();
                adminGUI.getEmployeeTableView().getItems().addAll(searchResults);
            } else {
                // If search field is empty, show all employees
                System.out.println("Search field is empty. Displaying all employees.");
                populateEmployeeList();
            }
        } else {
            System.out.println("Search field not found.");
        }
    }

    public void handleFilter() {
        ArrayList<String> jobTitles = administratorDAO.getAllJobTitles();  // Fetch job titles
        ArrayList<String> sections = administratorDAO.getAllSections();    // Fetch sections

        FilterEmployee filter = new FilterEmployee(jobTitles, sections);

        filter.show(primaryStage, () -> {
            ArrayList<String> selectedJobTitles = filter.getSelectedJobTitles();
            ArrayList<String> selectedSections = filter.getSelectedSections();

            ArrayList<Employee> filteredEmployees = administratorDAO.filterEmployees(selectedJobTitles, selectedSections);

            // Clear the table and add the filtered employees to it
            adminGUI.getEmployeeTableView().getItems().clear();
            adminGUI.getEmployeeTableView().getItems().addAll(filteredEmployees);
        });
    }

    public void handleAddEmployee() {

    }
    public void handleEditEmployee(String nameField, String roleField, String sectionField, String emailField, String phoneField) {
        if (emailField.isEmpty() || nameField.isEmpty() || phoneField.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required!");
            return;
        }
        Employee employee = administratorDAO.getEmployeeByEmail(emailField);
        if (employee == null) {
            showAlert(Alert.AlertType.ERROR, "Employee Not Found", "No employee found with this email.");
            return;
        }
        employee.setName(nameField);
        employee.setRole(roleField);
        employee.setSectionName(sectionField);
        employee.setPhoneNum(phoneField);
        boolean updateSuccess = administratorDAO.updateEmployee(employee);
        if (updateSuccess) {
            showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Employee details have been updated successfully.");
            populateEmployeeList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update employee details. Please try again.");
        }
    }

    public void handleDeleteEmployee(Employee selectedEmployee) {
        if (selectedEmployee != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete the selected employee?");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean success = employeeService.deleteEmployee(selectedEmployee.getEmployeeId());
                    if (success) {
                        System.out.println("Employee deleted successfully: " + selectedEmployee.getName());
                        showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Employee deleted successfully!");
                        populateEmployeeList();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Failed to delete employee. Please try again.");
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No artifact selected for deletion.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType); alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(message); alert.showAndWait();
    }
}
