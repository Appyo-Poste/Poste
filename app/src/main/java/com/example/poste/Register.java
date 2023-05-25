package com.example.poste;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.PosteApplication;
import com.example.poste.database.AppRepository;
import com.example.poste.database.entity.User;

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

public class Register extends AppCompatActivity {

    EditText nameView, emailView, passwordView, confirmedPasswordView;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

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
                new Register.AsyncLogin().execute(nickname, username, password);
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

                // Enter URL address where your php file resides
                url = new URL("https://test-project-379806.wl.r.appspot.com/users/add");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("nickname", params[0])
                        .appendQueryParameter("username", params[1])
                        .appendQueryParameter("password", params[2]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    InputStream input = conn.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    String error = result.toString();
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            try
            {
                int id = Integer.parseInt(result);
                Toast.makeText(Register.this, "User created with ID: " + id, Toast.LENGTH_LONG).show();
                navigateToDashboard();
            } catch (NumberFormatException ex)
            {
                Toast.makeText(Register.this, ex.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    void navigateToDashboard(){
        finish();
        Intent intent = new Intent(Register.this, Dashboard.class);
        startActivity(intent);
    }
}