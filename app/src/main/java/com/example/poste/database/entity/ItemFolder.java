package com.example.poste.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(foreignKeys = {@ForeignKey(entity = PosteItem.class,
        parentColumns = "poste_item_id",
        childColumns = "poste_item_id",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Folder.class,
                parentColumns = "folder_id",
                childColumns = "folder_id",
                onDelete = ForeignKey.CASCADE)},
        primaryKeys = {"poste_item_id","folder_id"})
public class ItemFolder {
    @NonNull
    public String poste_item_id;
    @NonNull
    public String folder_id;
}
