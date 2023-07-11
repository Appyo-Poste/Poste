package com.example.legacy.poste.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class User {
    @PrimaryKey
    @NonNull
public String email;
public String password;
public String name;
}
