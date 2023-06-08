package com.example.poste.api.models;

public class Post {

    private int id;
    private String name;
    private String link;
    private int ownerId;

    public Post(int id, String name, String link, int ownerId) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.ownerId = ownerId;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
