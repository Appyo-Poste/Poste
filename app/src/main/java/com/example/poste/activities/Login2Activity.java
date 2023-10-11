package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.poste.R;

public class Login2Activity extends AppCompatActivity {

    public Button buttonLoginSubmit;
    private EditText usernameField,passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        usernameField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);

        buttonLoginSubmit = findViewById(R.id.loginLoginbtn);

        buttonLoginSubmit.setOnClickListener(view ->
                buttonTestFunc());
    }

    public void buttonTestFunc(){
        String email,password;
        email = usernameField.getText().toString();
        password = passwordField.getText().toString();
        Toast.makeText(Login2Activity.this, "Email: "+email + "\nPassword: " + password, Toast.LENGTH_LONG).show();
    }
}