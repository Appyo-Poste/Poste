package com.example.poste.http;

public class FolderRequest {
    private String title;
    private int creator;

    public FolderRequest(String title, int userID) {
        this.title = title;
        this.creator = userID;
    }
}
