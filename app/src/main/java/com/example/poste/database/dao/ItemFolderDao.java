package com.example.poste.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.poste.database.entity.ItemFolder;

import java.util.List;

@Dao
public interface ItemFolderDao {
    @Insert
    void insert(ItemFolder itemFolder);
    @Insert
    void insert(List<ItemFolder> itemFolderList);

    @Query("SELECT * FROM ItemFolder")
    List<ItemFolder> getItemFolders();

    @Query("SELECT * FROM ItemFolder WHERE folder_id = :id")
    ItemFolder getItemFolderByFolder(String id);

    @Query("SELECT * FROM ItemFolder WHERE poste_item_id = :id")
    ItemFolder getItemFolderByItem(String id);

    @Update
    void update(ItemFolder itemFolder);

    @Query("SELECT * FROM ItemFolder WHERE poste_item_id = :itemId AND folder_id = :folderId")
    ItemFolder getItemFolder(String itemId, String folderId);
}
