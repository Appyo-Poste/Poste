package com.example.poste.http;

public class EditAccountRequest {
    private String oldPassword;
    private String newPassword;

    public EditAccountRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
