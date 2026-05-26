package com.project.back_end.DTO;

public class Login {

    // username (admin) OR email (doctor/patient)
    private String identifier;

    // login password
    private String password;


    // Getter
    public String getIdentifier() {
        return identifier;
    }

    // Setter
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    // Getter
    public String getPassword() {
        return password;
    }

    // Setter
    public void setPassword(String password) {
        this.password = password;
    }

}