package com.example.poste.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.poste.database.entity.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE email = :email")
    User getUser(String email);

    @Query("SELECT email FROM user LIMIT 1")
    String getFirstUsername();

    @Insert
    void insert(User user);
}
