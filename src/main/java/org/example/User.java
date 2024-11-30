package org.example;

public abstract class User {
    private String userId;
    private String password;
    private String name;
    private String emailAddress;
    private String section;

    public User(String userId, String password, String name, String emailAddress, String section) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.emailAddress = emailAddress;
        this.section = section;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
