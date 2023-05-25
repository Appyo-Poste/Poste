package com.example.poste.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.poste.database.entity.Folder;

import java.util.List;

@Dao
public interface FolderDao {
    @Query("SELECT * FROM folder WHERE folder_id = :id")
    Folder getFolder(String id);
    @Query("SELECT * FROM Folder " +
            "INNER JOIN UserFolder ON Folder.folder_id = UserFolder.folder_id " +
            "WHERE UserFolder.email LIKE :username")
    List<Folder> foldersByUserName(String username);

    @Insert
    long insert(Folder folder);
    @Query("SELECT folder_id FROM folder WHERE rowId = :rowId")
    int getNewInsertId(long rowId);

    @Update
    void update(Folder folder);

    @Delete
    void delete(Folder folder);
}
