package com.example.poste.http;

public class EditPostRequest {
    public EditPostRequest(String title, String description, String url, String tags) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.tags = tags;
    }
    private String title;
    private String description;
    private String url;
    private String tags;
}
