package com.example.poste.activities;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;
import com.example.poste.api.twitter.OAuth20GetAccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.twitter.clientlib.model.Tweet;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class TwitterTokenActivity extends AppCompatActivity {

    private Application currentApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_token);

        View authorizeBtn = findViewById(R.id.twitter_token_authorize_btn);
        View getUserBtn = findViewById(R.id.twitter_token_api_call_btn);
        View getTweetsBtn = findViewById(R.id.twitter_token_tweet_btn);
        currentApplication = this.getApplication();

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = intent.getData();

        if (Intent.ACTION_VIEW.equals(action)) {
            if (uri != null) {
                Log.d("Hello", uri.getQuery());
                HashMap<String, String> result = parseResult(uri.getQuery());
                String getCode = result.get("code");
                new AsyncTwitterAuth().execute(result.get("code"));
            }
        }

        authorizeBtn.setOnClickListener(view -> {
            String twitterUrl = OAuth20GetAccessToken.getAuthUrl();
            openWebPage(twitterUrl);
        });

        getUserBtn.setOnClickListener(view -> {
            new AsyncTwitterCall().execute();
        });
        getTweetsBtn.setOnClickListener(view -> {
            new AsyncGetTweets().execute();
        });

    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    protected HashMap<String, String> parseResult(String result){
        String[] resultArray = result.split("&");
        HashMap<String, String> tokens = new HashMap<>();
        String[] parseItem;
        for (String item: resultArray) {
            parseItem = item.split("=");
            tokens.put(parseItem[0], parseItem[1]);
        }
        return tokens;
    }

    private class AsyncTwitterAuth extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(TwitterTokenActivity.this);
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
            OAuth2AccessToken accessToken = null;
            try {
                accessToken = OAuth20GetAccessToken.getAccessToken(params[0]);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            if (accessToken != null){
//                AppRepository appRepository = new AppRepository(currentApplication);
//                TwitterToken token = appRepository.getTwitterToken();
//                long id;
//                Folder folder;
//                if(token == null) {
//                    token = new TwitterToken();
//                    token.access_token = accessToken.getAccessToken();
//                    token.refresh_token = accessToken.getRefreshToken();
//                    folder = new Folder();
//                    folder.folder_name = "Twitter";
//                    folder.shared = false;
//                    id = appRepository.insertFolder(folder);
//                    User user = appRepository.getUser(PosteApplication.getLoggedInUser());
//                    if(user != null){
//                        UserFolder userFolder = new UserFolder();
//                        userFolder.folder_id = String.valueOf(id);
//                        userFolder.email = user.email;
//                        appRepository.insertUserFolder(userFolder);
//                    }
//                    appRepository.updateTwitterToken(token.access_token, token.refresh_token, String.valueOf(id));
//                }else{
//                    token.access_token = accessToken.getAccessToken();
//                    token.refresh_token = accessToken.getRefreshToken();
//                   folder = appRepository.getFolder(token.twitter_folder_id);
//                   if(folder == null){
//                       folder = new Folder();
//                       folder.folder_name = "Twitter";
//                       folder.shared = false;
//                       id = appRepository.insertFolder(folder);
//                       User user = appRepository.getUser(PosteApplication.getLoggedInUser());
//                       if(user != null){
//                           UserFolder userFolder = new UserFolder();
//                           userFolder.folder_id = String.valueOf(id);
//                           userFolder.email = user.email;
//                           appRepository.insertUserFolder(userFolder);
//                       }
//                   }else {
//                       id = folder.folder_id;
//                   }
//                    appRepository.updateTwitterToken(token.access_token, token.refresh_token, String.valueOf(id));
//                }
                return "Successful";
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            try {
                Toast.makeText(TwitterTokenActivity.this, result, Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                Toast.makeText(TwitterTokenActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    private class AsyncTwitterCall extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(TwitterTokenActivity.this);
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
                String result = OAuth20GetAccessToken.apiCall(currentApplication);
//                AppRepository appRepository = new AppRepository(currentApplication);
//                appRepository.setTwitterId(result);

                return result;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            try {
                Toast.makeText(TwitterTokenActivity.this, result, Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
                Toast.makeText(TwitterTokenActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    private class AsyncGetTweets extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(TwitterTokenActivity.this);
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
                List<Tweet> result = OAuth20GetAccessToken.getTweets();
                return result.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            startActivity(new Intent(TwitterTokenActivity.this, DashboardActivity.class));

        }
    }

}