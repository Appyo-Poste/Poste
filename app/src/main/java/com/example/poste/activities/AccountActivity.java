package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.example.poste.R;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account);

        // Prep vars
        Button buttonSave = (Button) findViewById(R.id.AccountSavebtn);

        // Save account button
        buttonSave.setOnClickListener(view -> {
            // Send to login page
            Intent intent = new Intent(AccountActivity.this, Login.class);
            startActivity(intent);
        });
    }
}