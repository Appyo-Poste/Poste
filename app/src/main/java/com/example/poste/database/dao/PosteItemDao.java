package com.example.poste.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.poste.database.entity.Folder;
import com.example.poste.database.entity.PosteItem;

import java.util.List;

@Dao
public interface PosteItemDao {
    @Insert
    void insert(PosteItem tweet);
    @Insert
    void insert(List<PosteItem> tweets);

    @Query("SELECT * FROM PosteItem")
    List<PosteItem> getTweets();

    @Query("SELECT * FROM PosteItem " +
            "INNER JOIN ItemFolder ON ItemFolder.poste_item_id = PosteItem.poste_item_id " +
            "WHERE ItemFolder.folder_id LIKE :folderId")
    List<PosteItem> postsByFolder(String folderId);

    @Query("SELECT * FROM PosteItem WHERE poste_item_id = :id")
    PosteItem getTweet(String id);

    @Update
    void update(PosteItem tweet);
}
