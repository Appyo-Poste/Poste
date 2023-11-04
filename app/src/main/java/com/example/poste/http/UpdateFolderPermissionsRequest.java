package com.example.poste.http;

import com.example.poste.api.poste.models.FolderAccess;

public class UpdateFolderPermissionsRequest {
    public UpdateFolderPermissionsRequest(String folderId, String email, FolderAccess permission) {
        this.folderId = folderId;
        // this is necessary else the user is sometimes not found
        this.email = email.toLowerCase();
        this.permission = permission;
    }
    private String folderId;
    private String email;
    private FolderAccess permission;
}
