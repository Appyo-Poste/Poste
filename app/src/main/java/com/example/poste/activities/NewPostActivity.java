package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;


import com.example.poste.R;
import com.example.poste.callbacks.UpdateCallback;
import com.example.poste.models.Folder;
import com.example.poste.models.User;

import java.util.List;

import javax.annotation.Nullable;

public class NewPostActivity extends AppCompatActivity {

    private String title;

    private String link;

    private String description;

    private String tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        if (User.getUser().getToken() == null || User.getUser().getToken().isEmpty()) {
            // user needs to login
            Log.d("NewPostActivity", "User needs to login");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.putExtra("return", true);
            startActivity(loginIntent);
        } else {
            Log.d("NewPostActivity", "User is logged in");
            Log.d("NewPostActivity", "User token: " + User.getUser().getToken());
        }
        handleSharedContent(getIntent());

    }


    private void handleSharedContent(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            EditText linkEditText = findViewById(R.id.editTextPostLink);
            linkEditText.setText(sharedText);
        }
    }
}
