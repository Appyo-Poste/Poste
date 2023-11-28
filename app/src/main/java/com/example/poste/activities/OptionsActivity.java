package com.example.poste.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;
import com.example.poste.models.User;

/**
 * The OptionsActivity class adds functionality to the activity_options.xml layout
 */
public class OptionsActivity extends AppCompatActivity {

    Button Signoutbtn;

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure window settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Set the activity layout
        setContentView(R.layout.activity_options);

        Signoutbtn = findViewById(R.id.Signoutbtn);

        Signoutbtn.setOnClickListener(view -> {
            User.getUser().logout();
            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        Button accountSettings = findViewById(R.id.options_accounts_btn);
        accountSettings.setOnClickListener(view ->
        {
            Intent accountSettingsIntent = new Intent(OptionsActivity.this, AccountActivity.class);
            startActivity(accountSettingsIntent);
            finish();
        });
    }
}