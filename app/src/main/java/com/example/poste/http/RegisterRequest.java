package com.example.poste.http;

public class RegisterRequest {
    private String email;

    private String first_name;

    private String last_name;
    private String password;

    public RegisterRequest(String email, String first_name, String last_name, String password) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
    }
}
