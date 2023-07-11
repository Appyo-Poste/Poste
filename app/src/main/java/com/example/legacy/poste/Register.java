package com.example.legacy.poste;

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

import com.example.legacy.PosteApplication;
import com.example.legacy.poste.api.API;
import com.example.legacy.poste.api.exceptions.APIException;
import com.example.legacy.poste.database.AppRepository;
import com.example.legacy.poste.database.entity.User;
import com.example.poste.R;

public class Register extends AppCompatActivity {

    EditText nameView, emailView, passwordView, confirmedPasswordView;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static com.example.legacy.poste.api.models.User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        nameView = findViewById(R.id.RNameTxt);
        emailView = findViewById(R.id.REmailTxt);
        passwordView = findViewById(R.id.RPasswordTxt);
        confirmedPasswordView = findViewById(R.id.RConfirmPasswordTxt);

        Button submit = findViewById(R.id.RCreateAccountBtn);
        submit.setOnClickListener(v -> {
            String nickname = nameView.getText().toString();
            String username = emailView.getText().toString();
            String password = passwordView.getText().toString();
            String confirmedPassword = confirmedPasswordView.getText().toString();
            if(password.equals(confirmedPassword))
            {
                User user = new User();
                user.email = username;
                user.password = password;
                user.name = nickname;

                AppRepository appRepository = new AppRepository(PosteApplication.getApp());
                appRepository.insertUser(user);
                new AsyncLogin().execute(nickname, username, password);
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
        ProgressDialog pdLoading = new ProgressDialog(Register.this);

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
                API.addUser(params[1], params[0], params[2]);

                newUser = API.getUserByEmail(params[1]);

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
                    Toast.makeText(Register.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
                    PosteApplication.setLoggedInUser(newUser.getEmail());
                    PosteApplication.setCurrentUser(newUser);
                    navigateToDashboard();
                    break;
                case "EmailAlreadyUsedException":
                    Toast.makeText(Register.this, getString(R.string.register_email_in_use), Toast.LENGTH_LONG).show();
                    break;
                case "IncompleteRequestException":
                case "MalformedResponseException":
                    Toast.makeText(Register.this, getString(R.string.internal_error), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(Register.this, getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    void navigateToDashboard(){
        finish();
        Intent intent = new Intent(Register.this, Dashboard.class);
        startActivity(intent);
    }
}