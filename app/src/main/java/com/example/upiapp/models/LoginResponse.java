package com.example.upiapp.models;

public class LoginResponse {
    public String token; // [cite: 24]
    public UserData user; // [cite: 25]

    public static class UserData {
        public String upiId; // Corrected from upild [cite: 27]
        public String name; // [cite: 28]
    }
}