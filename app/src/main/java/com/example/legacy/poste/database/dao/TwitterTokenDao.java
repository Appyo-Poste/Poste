package com.example.legacy.poste.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.legacy.poste.database.entity.TwitterToken;

@Dao
public interface TwitterTokenDao {
    @Query("SELECT * FROM twittertoken LIMIT 1")
    TwitterToken getToken();

    @Insert
    void insert(TwitterToken token);

    @Query("UPDATE twittertoken SET access_token = :access_token, refresh_token = :refresh_token, twitter_folder_id = :twitter_folder_id WHERE access_token = :existing_access_token")
    void update(String existing_access_token, String access_token, String refresh_token, String twitter_folder_id);

    @Query("UPDATE twittertoken SET twitter_id = :id")
    void setId(String id);
}
