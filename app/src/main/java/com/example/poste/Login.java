package com.example.poste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.PosteApplication;
import com.example.poste.api.API;
import com.example.poste.api.exceptions.APIException;
import com.example.poste.api.exceptions.IncompleteRequestException;
import com.example.poste.api.exceptions.MalformedResponseException;
import com.example.poste.api.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {

    //public Button buttonForgotPassword;
    public Button buttonLoginSubmit;
    private Switch rememberMeSwitch;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;


    private EditText usernameField,passwordField;
    private String email;
    private String password;

    private User loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.LoginEmailEntertxt);
        passwordField = findViewById(R.id.LoginPassword);
        rememberMeSwitch = findViewById(R.id.login_remember_me_switch);

        //Email = findViewById(R.poste_item_id.LoginEmailEntertxt);

        //Page Navigation starts
        //buttonForgotPassword = findViewById(R.id.ForgotPasswordbtn);
        buttonLoginSubmit = findViewById(R.id.LoginLoginbtn);
        //Creating the link to AccountPage page
        /*buttonForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Account.class);
            startActivity(intent);
        });*/

        //Creating the link to Dashboard page
        buttonLoginSubmit.setOnClickListener(view -> checkLogin());
        //Page Navigation ends
    }

    void navigateToDashboard(){
        finish();
        Intent intent = new Intent(Login.this, Dashboard.class);
        startActivity(intent);
    }

    // Triggers when LOGIN Button clicked
    public void checkLogin() {

        // Get text from email and password field
        email = usernameField.getText().toString();
        password = passwordField.getText().toString();

        if(email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Please enter your login details", Toast.LENGTH_LONG).show();
        else {
            // Initialize  AsyncLogin() class with email and password
            new AsyncLogin().execute(email, password);
        }
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Login.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                boolean validLogin = API.validateUserLogin(params[0], params[1]);

                loginUser = API.getUserByEmail(params[0]);
                if (validLogin) {
                    return "ValidLogin";
                } else {
                    return "InvalidLogin";
                }
            } catch (APIException e) {
                e.printStackTrace();
                return e.getClass().toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            switch (result) {
                case "ValidLogin":
                    Toast.makeText(Login.this, getString(R.string.login_successful), Toast.LENGTH_LONG).show();
                    PosteApplication.setLoggedInUser(loginUser.getEmail());
                    navigateToDashboard();
                    break;
                case "InvalidLogin":
                    Toast.makeText(Login.this, getString(R.string.login_invalid_credentials), Toast.LENGTH_LONG).show();
                    break;
                case "NoUserFoundException":
                case "IncompleteRequestException":
                case "MalformedResponseException":
                    Toast.makeText(Login.this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(Login.this, getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}