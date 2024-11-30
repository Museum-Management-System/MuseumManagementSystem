package org.example;

public class Employee extends User {

    private String jobTitle;
    private String role; // "Curator" or "Guide"



    public Employee(String userId, String password, String name, String emailAddress, String section) {
        super(userId, password, name, emailAddress, section);
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



}


