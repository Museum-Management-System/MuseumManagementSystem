package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.example.dao.AdministratorDAO;
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

    public void handleAddEmployee(Employee addedEmployee, String password) {
        try {
            if (employeeService.addEmployee(addedEmployee, password)) {
                System.out.println("Employee added successfully: " + addedEmployee.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Successful");
                alert.setHeaderText(null);
                alert.setContentText("Employee added successfully!");
                alert.showAndWait();

                populateEmployeeList();
            } else {
                System.out.println("Failed to add employee: " + addedEmployee.getName());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add employee. Please try again.");
                alert.showAndWait();
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleUpdateEmployee(Employee updatedEmployee) {
        try {
            if (employeeService.updateEmployee(updatedEmployee)) {
                System.out.println("Employee updated successfully: " + updatedEmployee.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setHeaderText(null);
                alert.setContentText("Employee updated successfully!");
                alert.showAndWait();

                populateEmployeeList();
            } else {
                System.out.println("Failed to update employee: " + updatedEmployee.getName());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update employee. Please try again.");
                alert.showAndWait();
            }
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
