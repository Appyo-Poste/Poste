package com.example.poste.http;

public class EditPostRequest {
    public EditPostRequest(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
    private String title;
    private String description;
    private String url;

}
