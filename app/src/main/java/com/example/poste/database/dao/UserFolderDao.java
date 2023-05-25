package com.example.poste.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.poste.database.entity.UserFolder;

import java.util.List;

@Dao
public interface UserFolderDao {
    @Insert
    void insert(UserFolder userFolder);
    @Insert
    void insert(List<UserFolder> userFolderList);

    @Query("SELECT * FROM UserFolder")
    List<UserFolder> getUserFolders();

    @Query("SELECT * FROM UserFolder WHERE folder_id = :id")
    UserFolder getUserFolderByFolder(String id);

    @Query("SELECT * FROM UserFolder WHERE folder_id = :id")
    UserFolder getUserFolderByUser(String id);

    @Update
    void update(UserFolder userFolder);
}
