package org.example.entity;

public  class User {
    private int userId;        // user_id is now an integer as it's a serial type in the table
    private String userType;   // user_type to match the table definition ('Admin' or 'Employee')
    private String password;   // password field

    public User(int userId, String userType, String password) {
        this.userId = userId;
        this.userType = userType;
        this.password = password;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
