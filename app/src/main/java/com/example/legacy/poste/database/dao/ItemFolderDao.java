package com.example.legacy.poste.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemFolderDao {
    @Insert
    void insert(com.example.legacy.poste.database.entity.ItemFolder itemFolder);
    @Insert
    void insert(List<com.example.legacy.poste.database.entity.ItemFolder> itemFolderList);

    @Query("SELECT * FROM ItemFolder")
    List<com.example.legacy.poste.database.entity.ItemFolder> getItemFolders();

    @Query("SELECT * FROM ItemFolder WHERE folder_id = :id")
    com.example.legacy.poste.database.entity.ItemFolder getItemFolderByFolder(String id);

    @Query("SELECT * FROM ItemFolder WHERE poste_item_id = :id")
    com.example.legacy.poste.database.entity.ItemFolder getItemFolderByItem(String id);

    @Update
    void update(com.example.legacy.poste.database.entity.ItemFolder itemFolder);

    @Query("SELECT * FROM ItemFolder WHERE poste_item_id = :itemId AND folder_id = :folderId")
    com.example.legacy.poste.database.entity.ItemFolder getItemFolder(String itemId, String folderId);
}
