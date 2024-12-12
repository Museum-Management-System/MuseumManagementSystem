package org.example.entity;

import java.sql.*;
import org.example.service.DatabaseConnection;


public class Employee /*extends User*/ {
    private int employeeId;
    private String name;
    private String email;
    private String phoneNum;
    private String jobTitle;
    private String sectionName;
    private String role;

    public Employee(int employeeId, String name, String email, String jobTitle, String phoneNum, String  sectionName, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.jobTitle = jobTitle;
        this.sectionName = sectionName;
        this.role = role;
    }

    public int getEmployeeId() {return employeeId;}

    public void setEmployeeId(int employeeId) {this.employeeId = employeeId;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPhoneNum() {return phoneNum;}

    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}

    public String getJobTitle() {return jobTitle;}

    public void setJobTitle(String jobTitle) {this.jobTitle = jobTitle;}

    public String getSectionName() {return sectionName;}

    public void setSectionName(String sectionName) {this.sectionName = sectionName;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}
}
