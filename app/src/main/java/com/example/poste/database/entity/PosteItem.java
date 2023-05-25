package com.example.poste.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class PosteItem {
    @NonNull
    @PrimaryKey
    public String poste_item_id;
    public String text;
    public String media_key;
    public String media_url;
}
