package com.example.poste.http;

public class CreatePost {
    private String title;

    private String description;

    private String url;
    private String folder_id;

    public CreatePost(String title, String description, String url, String folder_id) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.folder_id = folder_id;
    }
}
