package com.example.poste.http;

public class CreatePost {
    private String title;

    private String description;

    private String url;
    private String folder;

    public CreatePost(String title, String description, String url, String folder) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.folder = folder;
    }
}
