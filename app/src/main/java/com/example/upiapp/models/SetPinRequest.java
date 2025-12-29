package com.example.upiapp.models;

public class SetPinRequest {
    public String password; // User's login password [cite: 34]
    public String newPin;   // The new 4-digit UPI PIN [cite: 35]

    public SetPinRequest(String password, String newPin) {
        this.password = password;
        this.newPin = newPin;
    }
}
