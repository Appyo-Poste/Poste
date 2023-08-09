package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;

import com.example.poste.R;

/**
 * The AccountActivity class adds functionality to the activity_account.xml layout
 */
public class AccountActivity extends AppCompatActivity {

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure window settings for fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Set the activity layout
        setContentView(R.layout.activity_account);

        // Prep vars
        Button buttonSave = findViewById(R.id.AccountSavebtn);

        // Click listener for the save account button
        buttonSave.setOnClickListener(view -> {
            // Send to login page
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}