package com.example.legacy.poste;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.example.poste.activities.Login;
import com.example.poste.R;

public class Account extends AppCompatActivity {
    public Button buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account);



        buttonSave = (Button) findViewById(R.id.AccountSavebtn);
        buttonSave.setOnClickListener(new View.OnClickListener() { //Creating the link to Login page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account.this, Login.class);
                startActivity(intent);
            }
        });
    }
}