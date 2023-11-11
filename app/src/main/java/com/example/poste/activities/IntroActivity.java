package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;

import com.example.poste.R;

/**
 * The IntroActivity class adds functionality to the activity_intro.xml layout
 * This is the first page the user sees when they open the app
 */
public class IntroActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_intro);

        // Prep vars
        Button buttonRegister = findViewById(R.id.ACCreateAccountbtn2);
        Button buttonLogin = findViewById(R.id.ACCLoginbtn);

        // Click listener for the register button -- takes user to register page (RegisterActivity)
        buttonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        // Click listener for the login button -- takes user to login page (LoginActivity)
        buttonLogin.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}