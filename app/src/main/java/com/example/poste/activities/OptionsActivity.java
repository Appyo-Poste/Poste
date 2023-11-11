package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.poste.R;
import com.example.poste.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * The OptionsActivity class adds functionality to the activity_options.xml layout
 */
public class OptionsActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button Signoutbtn;

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
        setContentView(R.layout.activity_options);

        Signoutbtn = findViewById(R.id.Signoutbtn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

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