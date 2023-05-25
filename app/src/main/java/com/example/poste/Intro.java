package com.example.poste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class Intro extends AppCompatActivity {
    public Button buttonRegister;
    public Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intro);

        buttonRegister = (Button) findViewById(R.id.ACCreateAccountbtn2);
        buttonLogin = (Button) findViewById(R.id.ACCLoginbtn);
        buttonRegister.setOnClickListener(new View.OnClickListener() { //Creating the link to Register page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intro.this,Register.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() { //Creating the link to Login page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intro.this,Login.class);
                startActivity(intent);
            }
        });
    }
}