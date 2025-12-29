package com.example.upiapp.models;

public class LoginRequest {
    public String upiId; // Matches contract field [cite: 19]
    public String password; // Matches contract field [cite: 20]

    public LoginRequest(String upild, String password) {
        this.upiId = upild;
        this.password = password;
    }
}