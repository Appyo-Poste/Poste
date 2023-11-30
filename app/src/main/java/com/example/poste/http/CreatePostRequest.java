package com.example.poste.http;

public class CreatePostRequest {
    private String title;
    private String description;
    private String url;
    private String folder_id;
    private String tags;

    public CreatePostRequest(String title, String description, String url, String folder_id, String tags) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.folder_id = folder_id;
        this.tags = tags;
    }
}
