package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.poste.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
    }
}