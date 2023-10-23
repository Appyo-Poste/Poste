package com.example.poste.http;

public class PostRequest {
    private String title;
    private String description;
    private String url;
    private String creator;
    private int folder;

    public PostRequest(String title, String description, String url, String creator, int folder) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.creator = creator;
        this.folder = folder;
    }
}
