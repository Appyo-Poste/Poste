package com.example.poste.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;
import com.example.poste.models.User;

/**
 * The IntroActivity class adds functionality to the activity_intro.xml layout
 * This is the first page the user sees when they open the app
 */
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        User user = User.getUser();
        if (!user.getToken().equals("")) {
            Intent intent = new Intent(IntroActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = User.getUser();
        if (user.getToken().isEmpty()) {
            boolean rememberMe = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE).getBoolean("rememberMe", false);
            if (rememberMe) {
                String prefToken = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE).getString("token", "");
                if (!prefToken.isEmpty()) {
                    user.setToken(prefToken);
                }
            }
        }
        if (!user.getToken().isEmpty()) {
            Intent intent = new Intent(IntroActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }

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
        });
    }
}