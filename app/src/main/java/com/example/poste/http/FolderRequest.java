package com.example.poste.http;

public class FolderRequest {
    private String title;
    private String creator;

    public FolderRequest(String title, String creator) {
        this.creator = creator;
        this.title = title;
    }
}
