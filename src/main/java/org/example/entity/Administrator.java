package org.example.entity;

import java.sql.*;

public class Administrator {
    private int adminId;
    private String name;
    private String email;
    private String sectionName;

    public Administrator(int adminId, String name, String email, String sectionName) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.sectionName = sectionName;
    }
    public int getAdminId() {return this.adminId;}
    public String getName() {return this.name;}
    public String getEmail() {return this.email;}
    public String getSectionName() {return this.sectionName;}

    public void setAdminId(int adminId) {this.adminId = adminId;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setSectionName(String sectionName) {this.sectionName = sectionName;}
}