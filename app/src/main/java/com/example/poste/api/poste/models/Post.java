package com.example.poste.api.poste.models;

/**
 * This class is used to create objects that represent a post saved by the user
 */
public class Post {

    private int id;
    private String name;
    private String link;
    private int ownerId;

    /**
     * Constructor for a new post object
     *
     * @param id The ID number of the post, which is a number unique to the owner ID Associated with the post
     * @param name The name the creator of this post assigned to it
     * @param link The address of the orginal social media post that this object stores
     * @param ownerId The unique ID number of the user that created this post
     */
    public Post(int id, String name, String link, int ownerId) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.ownerId = ownerId;
    }

    /**
     * @return The post ID number
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The post ID number
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The name assigned to this post
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name assigned to this post
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The social media link stored in this post
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The social media link stored in this post
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return The ID number of this post's owner
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId The ID number of this post's owner
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
