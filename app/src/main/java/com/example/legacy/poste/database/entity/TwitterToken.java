package com.example.legacy.poste.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class TwitterToken {
    @PrimaryKey
    @NonNull
    public String access_token;
    public String refresh_token;
    public String twitter_id;
    public String twitter_folder_id;
}
