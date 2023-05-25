package com.example.poste.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Folder {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int folder_id;
    public String folder_name;
    public boolean shared;
}
