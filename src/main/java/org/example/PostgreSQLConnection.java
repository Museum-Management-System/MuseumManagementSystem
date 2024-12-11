package org.example;

import org.example.controller.LoginController;
import org.example.dao.UserDAO;
import org.example.view.LoginView;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQLConnection {


    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school", "postgres", "");

            UserDAO userDAO = new UserDAO(connection);
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, userDAO);
            loginView.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
