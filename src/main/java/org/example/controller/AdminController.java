package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.dao.AdministratorDAO;
import org.example.dao.MuseumArtifactDAO;
import org.example.entity.Employee;
import org.example.entity.MuseumArtifact;
import org.example.service.DatabaseConnection;
import org.example.service.EmployeeService;
import org.example.view.GUIs.AdminGUI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminController extends EmployeeController{

    private AdministratorDAO administratorDAO;
    private EmployeeService employeeService;
    private AdminGUI adminGUI;
    private Connection connection;
    public AdminController(AdminGUI adminGUI) throws SQLException {
        super(adminGUI);
        this.adminGUI = adminGUI;
        this.connection = DatabaseConnection.getConnection();
        this.administratorDAO = new AdministratorDAO(connection);
        this.employeeService = new EmployeeService(administratorDAO);
    }
    public void populateEmployeeList() {
        ArrayList<Employee> allEmployees = administratorDAO.searchEmployees();
        adminGUI.getEmployeeTableView().getItems().clear();
        adminGUI.getEmployeeTableView().getItems().addAll(allEmployees);
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
