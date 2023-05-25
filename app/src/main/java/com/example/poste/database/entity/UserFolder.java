package com.example.poste.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "email",
        childColumns = "email",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Folder.class,
                parentColumns = "folder_id",
                childColumns = "folder_id",
                onDelete = ForeignKey.CASCADE)},
        primaryKeys = {"email","folder_id"})
public class UserFolder {
    @NonNull
    public String email;
    @NonNull
    public String folder_id;
}
