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
import com.example.poste.api.poste.models.User;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    // Vars
    EditText nameView, emailView, passwordView, confirmedPasswordView;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        // Init vars
        nameView = findViewById(R.id.RNameTxt);
        emailView = findViewById(R.id.REmailTxt);
        passwordView = findViewById(R.id.RPasswordTxt);
        confirmedPasswordView = findViewById(R.id.RConfirmPasswordTxt);

        // Create account button
        Button submit = findViewById(R.id.RCreateAccountBtn);
        submit.setOnClickListener(v -> {
            String username = nameView.getText().toString();
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();
            String confirmedPassword = confirmedPasswordView.getText().toString();

            if(password.equals(confirmedPassword))
            {
                new RegisterActivity.AsyncLogin().execute(email, username, password);

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

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(RegisterActivity.this);

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
                API.addUser(params[0], params[1], params[2]);

                newUser = API.getUserByEmail(params[0]);

                return "UserCreated";
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

    void navigateToDashboard(){
        finish();
        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}