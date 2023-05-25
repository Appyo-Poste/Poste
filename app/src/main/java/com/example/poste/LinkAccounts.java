package com.example.poste;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LinkAccounts extends AppCompatActivity {

    // private Button buttonReddit;
    private Button buttonTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_link_accounts);





        /*buttonReddit = (Button) findViewById(R.id.redditbtn);
        buttonReddit.setOnClickListener(new View.OnClickListener() { //Creating the link to AccountPage page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LinkAccounts.this, redditInstructions.class);
                startActivity(intent);
            }
        }); */

        buttonTwitter = findViewById(R.id.start_twitter_activity_btn);
        buttonTwitter.setOnClickListener(new View.OnClickListener() { //Creating the link to AccountPage page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LinkAccounts.this, TwitterTokenActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void Reddit(){
        String username = "YOUR-REDDIT-USERNAME"; //Reddit username
        String password = "YOUR-REDDIT-PASSWORD"; //Reddit Password
        String clientId = "REDDIT-CLIENT-ID"; //Client ID
        String clientSecret = "REDDIT-CLIENT-SECRET"; //Client Secret
        String stringUrl = "http://www.reddit.com/api/v1/access-token"; //Reddit url endpoint

        /* curl 1
        curl -X POST -d 'grant_type=password&username=reddit_bot&password=snoo' --user 'p-jcoLKBynTLew:gko_LXELoV07ZBNUXrvWZfzE3aI' https://www.reddit.com/api/v1/access_token
         */

        /*  curl 2
        curl -H "Authorization: bearer J1qK1c18UUGJFAzz9xnH56584l4" -A "ChangeMeClient/0.1 by YourUsername" https://oauth.reddit.com/api/v1/me
         */
    }
}