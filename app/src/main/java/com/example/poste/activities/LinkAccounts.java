package com.example.poste.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;

/**
 * The LinkAccounts class adds functionality to the activity_link_account.xml layout
 */
public class LinkAccounts extends AppCompatActivity {
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
        setContentView(R.layout.activity_link_accounts);

        // Prep vars
        Button buttonTwitter = findViewById(R.id.start_twitter_activity_btn);

        // Click listener for the twitter button
        buttonTwitter.setOnClickListener(view -> {
            Intent intent = new Intent(LinkAccounts.this, TwitterTokenActivity.class);
            startActivity(intent);
        });
    }
}