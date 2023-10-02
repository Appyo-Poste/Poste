package com.example.poste.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.exceptions.EmailAlreadyUsedException;
import com.example.poste.api.poste.models.User;

import java.util.Objects;

/**
 * The RegisterActivity class adds functionality to the activity_register.xml layout
 * Called when user clicks on the register button on the intro page
 */
public class RegisterActivity extends AppCompatActivity {

    // Vars
    EditText nameView, emailView, passwordView, confirmedPasswordView;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static User newUser;

    /**
     * Called when the activity is created (when user clicks on register button on intro page)
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
        setContentView(R.layout.activity_register);

        // Init vars
        nameView = findViewById(R.id.RNameTxt);
        emailView = findViewById(R.id.REmailTxt);
        passwordView = findViewById(R.id.RPasswordTxt);
        confirmedPasswordView = findViewById(R.id.RConfirmPasswordTxt);

        // Create account button
        Button submit = findViewById(R.id.RCreateAccountBtn);
        submit.setOnClickListener(v -> {
            String name = nameView.getText().toString();
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();
            String confirmedPassword = confirmedPasswordView.getText().toString();

            if(password.equals(confirmedPassword))
            {
                // Necessary to avoid NetworkOnMainThreadException
                //new RegisterActivity.AsyncLogin().execute(email, username, password);
                try{
                    User newuser = User.create(email, name, password);
                } catch (EmailAlreadyUsedException e) {
                    throw new RuntimeException(e);
                }


//                User user = new User();
//                user.email = username;
//                user.password = password;
//                user.name = nickname;
//
//                AppRepository appRepository = new AppRepository(PosteApplication.getApp());
//                appRepository.insertUser(user);
//                new Register.AsyncLogin().execute(nickname, username, password);
                //finish();
            }
            else
            {
                Toast.makeText(this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
            }

        });
    }
/* On click for save will update the database by opening a connection string and then writing an SQL Statement
 then closing the connection string.
 */

    /**
     * Extends AsyncTask to perform API calls in background thread
     * This is necessary to avoid NetworkOnMainThreadException (API calls must be done in background
     * thread)
     * This is because API calls are network operations, and network operations must be done in
     * background thread for Android apps
     */
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(RegisterActivity.this);

        /**
         * Displays loading dialog while API call is being made
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        /**
         * Performs API call in background thread
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                // email, user, password
                API.addUser(params[0], params[1], params[2]);

                //newUser = API.getUserByEmail(params[0]);

                return "UserCreated";
            } catch (APIException e) {
                e.printStackTrace();
                return e.getClass().toString();
            }
        }

        /**
         * Called after API call is made. Based on result, either displays error or navigates to
         * dashboard
         *
         * @param result The result of the API call
         */
        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            pdLoading.dismiss();

            switch (result) {
                case "UserCreated":
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
                    PosteApplication.setCurrentUser(newUser);
                    navigateToDashboard();
                    break;
                case "EmailAlreadyUsedException":
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_email_in_use), Toast.LENGTH_LONG).show();
                    break;
                case "IncompleteRequestException":
                case "MalformedResponseException":
                    Toast.makeText(RegisterActivity.this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(RegisterActivity.this, getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    /**
     * Navigates to dashboard
     */
    void navigateToDashboard(){
        finish(); // Close this activity
        // Create intent to open dashboard
        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
        // Start dashboard activity
        startActivity(intent);
    }
}