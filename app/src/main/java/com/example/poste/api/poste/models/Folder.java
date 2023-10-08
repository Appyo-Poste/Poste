package com.example.poste.api.poste.models;

import com.example.poste.api.poste.API;

import java.util.HashMap;
import java.util.List;

/**
 * Class used to create folder objects
 */
public class Folder {

    private int id;
    private String name;
    private int ownerId;
    private List<Post> posts;
    private HashMap<Integer, FolderAccess> users;

    /**
     * Constructor for create a new folder object
     *
     * @param id The ID number of this folder
     * @param name The name the owner gives the folder
     * @param ownerId The ID number the the user that owns this folder
     * @param posts A list of posts contained within this folder
     * @param users A HashMap of the users that have a specific access level for this folder granted to them
     */
    public Folder(int id, String name, int ownerId, List<Post> posts, HashMap<Integer, FolderAccess> users) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.posts = posts;
        this.users = users;
    }

    /**
     * @return The ID number of this folder
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The ID number of this folder
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The name the owner gave this folder
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name the owner gave this folder
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The ID number the the user that owns this folder
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId The ID number the the user that owns this folder
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return The reference to the list of post contained in this folder
     */
    public List<Post> getPosts() {
        return posts;
    }

    /**
     * Replaces the entire post list within this folder with a new list passed as a parameter.
     * Used primarily for copying one folder into a new one.
     *
     * @param posts The reference to the new list of posts that will overwrite the current list
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    /**
     * @return The reference to the HashMap of user with their access levels
     */
    public HashMap<Integer, FolderAccess> getUsers() {
        return users;
    }

    /**
     * Replaces the HashMap of User Access levels with a new HashMap passed as a parameter.
     * Used primarily for copying one folder's access setting to another folder.
     *
     * @param users The reference to the new HashMap of user with their access levels
     */
    public void setUsers(HashMap<Integer, FolderAccess> users) {
        this.users = users;
    }

    /**
     * Updates the API with this folders current information
     *
     * @return true if successful
     */
    public boolean update() {
        try {
            return API.updateFolder(this.id, this.name, this.ownerId);
        } catch (Exception e) { return false; }
    }
}
