package com.example.poste.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.User;

/**
 * The LoginActivity class adds functionality to the activity_login.xml layout
 */
public class LoginActivity extends AppCompatActivity {
    public Button buttonLoginSubmit;
    private Switch rememberMeSwitch;
    private EditText usernameField,passwordField;
    private String email;
    private String password;
    private User loginUser;

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
        setContentView(R.layout.activity_login);

        // Prep vars
        usernameField = findViewById(R.id.LoginEmailEntertxt);
        passwordField = findViewById(R.id.LoginPassword);
        rememberMeSwitch = findViewById(R.id.login_remember_me_switch);

        //Email = findViewById(R.poste_item_id.LoginEmailEntertxt);

        //Page Navigation starts
        buttonLoginSubmit = findViewById(R.id.LoginLoginbtn);

        //Creating the link to Dashboard page
        buttonLoginSubmit.setOnClickListener(view -> checkLogin());
        //Page Navigation ends
    }

    void navigateToDashboard(){
        finish();
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }


    /**
     * Triggers when LOGIN Button clicked from LOGIN page.
     * Parses email and password from text fields, and sends an API call to validate user.
     * If user is validated, sets as current user and return to dashboard.
     * Otherwise, display error.
     */
    public void checkLogin() {
        email = usernameField.getText().toString();
        password = passwordField.getText().toString();
        if(email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Please enter your login details", Toast.LENGTH_LONG).show();
        else {
            try {
                User user = API.login(email, password);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                    PosteApplication.setCurrentUser(loginUser);
                    navigateToDashboard();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_invalid_credentials), Toast.LENGTH_LONG).show();
                }
            } catch (APIException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
            }

        }
    }
}