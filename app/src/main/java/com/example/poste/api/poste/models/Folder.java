package com.example.poste.api.poste.models;

import com.example.poste.api.poste.API;

import java.util.HashMap;
import java.util.List;

public class Folder {

    private int id;
    private String name;
    private int ownerId;
    private List<Post> posts;
    private HashMap<Integer, FolderAccess> users;

    public Folder(int id, String name, int ownerId, List<Post> posts, HashMap<Integer, FolderAccess> users) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.posts = posts;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public HashMap<Integer, FolderAccess> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, FolderAccess> users) {
        this.users = users;
    }

    public boolean update() {
        try {
            return API.updateFolder(this.id, this.name, this.ownerId);
        } catch (Exception e) { return false; }
    }
}
