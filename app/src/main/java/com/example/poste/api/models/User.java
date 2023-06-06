package com.example.poste.api.models;

import com.example.poste.api.API;
import com.example.poste.api.exceptions.EmailAlreadyUsedException;
import com.example.poste.api.exceptions.IncompleteRequestException;
import com.example.poste.api.exceptions.MalformedResponseException;
import com.example.poste.api.exceptions.NoUserFoundException;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;

    public User(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public static User create(String email, String username, String password) throws EmailAlreadyUsedException {
        try {
            API.addUser(email, username, password);

            return API.getUserByEmail(email);
        } catch (IncompleteRequestException | MalformedResponseException | NoUserFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        String oldUsername = this.username;
        this.username = username;
        if (!update()) { this.username = oldUsername; }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String oldPassword = this.password;
        this.password = password;
        if (!update()) { this.password = oldPassword; }
    }

    public boolean validateLogin() {
        try {
            return API.validateUserLogin(this.email, this.password);
        }  catch (IncompleteRequestException | MalformedResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean update() {
        try {
            return API.updateUser(this.email, this.username, this.password);
        } catch (Exception e) { return false; }
    }
}
