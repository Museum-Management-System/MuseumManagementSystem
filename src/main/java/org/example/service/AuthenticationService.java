package org.example.service;

import org.example.dao.UserDAO;

public class AuthenticationService {
    public static boolean validateUser(int userID, String password) {
        if(UserDAO.authenticateUser(userID, password) == "Admin" && UserDAO.authenticateUser(userID, password) == "Employee"){
            return true;
        }else{
            return false;
        }
    }

    public static String getUserRole(int userId, String password) {
        return UserDAO.authenticateUser(userId, password);
    }
}
