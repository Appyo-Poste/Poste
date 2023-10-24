package com.example.poste.http;

public class PostRequest {
    private String title;
    private String description;
    private String url;
    private int folder;

    public PostRequest(String title, String description, String url, int folder) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.folder = folder;
    }
}
