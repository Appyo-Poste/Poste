package com.example.poste.api.poste.models;

import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.exceptions.EmailAlreadyUsedException;
import com.example.poste.api.poste.exceptions.IncompleteRequestException;
import com.example.poste.api.poste.exceptions.MalformedResponseException;
import com.example.poste.api.poste.exceptions.NoUserFoundException;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;
    private List<Folder> folders;

    public User(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;

        folders = new ArrayList<>();
    }

    /**
     * Creates a new user via API call and returns the newly created user (by another API call)
     * Room for improvement here, as we don't need 2 API calls for this.
     * @param email The user's email
     * @param name The user's username
     * @param password The user's password
     * @return The newly created user
     * @throws EmailAlreadyUsedException If the email is already in use
     */
    public static User create(String email, String name, String password) throws EmailAlreadyUsedException {
        try {
            API.addUser(email, name, password);
            return API.getUserByEmail(email);
        } catch (APIException e) {
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

    /**
     * Adds a folder to the list of folders this user can access
     * @param folder the folder to be added to the users list of folders
     */
    public void addFolder (Folder folder) {
        folders.add(folder);
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public boolean validateLogin() {
        try {
            return API.validateUserLogin(this.email, this.password);
        }  catch (IncompleteRequestException | MalformedResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update() {
        try {
            return API.updateUser(this.email, this.username, this.password);
        } catch (Exception e) { return false; }
    }
}
