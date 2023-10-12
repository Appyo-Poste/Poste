package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poste.R;

public class Login2Activity extends AppCompatActivity {

    public Button buttonLoginSubmit;
    private EditText usernameField,passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        TextView hyperlinkTextView = findViewById(R.id.hyperlinkTextViewToRegister);
        usernameField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);

        buttonLoginSubmit = findViewById(R.id.loginLoginbtn);

        buttonLoginSubmit.setOnClickListener(view ->
                buttonTestFunc());

        SpannableString spannable = new SpannableString(hyperlinkTextView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Start the SecondActivity when the link is clicked
                Intent intent = new Intent(Login2Activity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };

        spannable.setSpan(clickableSpan, 0, hyperlinkTextView.getText().length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        hyperlinkTextView.setText(spannable);
        hyperlinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void buttonTestFunc(){
        String email,password;
        email = usernameField.getText().toString();
        password = passwordField.getText().toString();
        Toast.makeText(Login2Activity.this, "Email: "+email + "\nPassword: " + password, Toast.LENGTH_LONG).show();
    }
}