package com.example.legacy.poste;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.legacy.PosteApplication;
import com.example.poste.R;
import com.example.legacy.poste.api.API;
import com.example.legacy.poste.api.reddit.Data;
import com.example.legacy.poste.api.reddit.GetUser;
import com.example.legacy.poste.api.reddit.RedditApi;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class redditInstructions extends AppCompatActivity {
    public Button Submit;
    public Button GetData;

    public static String Email = "";


    //Authorize Link:  https://www.reddit.com/api/v1/authorize?client_id=CLIENT_ID&response_type=TYPE&state=RANDOM_STRING&redirect_uri=URI&duration=DURATION&scope=SCOPE_STRING
    //private String clientID = BuildConfig.; //Client ID From Reddit
    private String response_type = "code"; //code is the standered response type
    private String state = UUID.randomUUID().toString(); //Makes a random state string
//    private String redirect_uri = "https://test-project-379806.wl.r.appspot.com/twitter/auth"; //URI for redirect
    private String redirect_uri = API.URL("twitter/auth").toString(); //URI for redirect
    private String duration = "permanent"; //Permanent need for token
    private String scope = "history"; //Area need for acsess is the saved data belonging to history
    //Example: https://www.reddit.com/api/v1/authorize?client_id=MoAV7KPhPmso-1_4u38pYQ&response_type=code&state=Cat&redirect_uri=https://www.google.com&duration=permanent&scope=history


    private String AccountUrl = "https://oauth.reddit.com/user/{redditUsername}/saved";
    private TextView redditUsernametxt;
    private String apikeys = ""; //This should be the api key


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reddit_instructions);


        Submit = findViewById(R.id.redditSubmitInfobtn);
        redditUsernametxt = findViewById(R.id.redditUsernametxt);
        GetData = findViewById(R.id.GetSavedDatabtn);

        Submit.setOnClickListener(new View.OnClickListener() { //Submitting the info
            @Override
            public void onClick(View view) {
                Email = PosteApplication.getLoggedInUser(); //Calling the Login Email from the person who logged in in the Login Page

                AuthorizationURL("https://www.reddit.com/api/v1/authorize?client_id=MoAV7KPhPmso-1_4u38pYQ&response_type=code&state=" + state +
                        "&redirect_uri=" + redirect_uri + "&duration=permanent&scope=history");//Opens the link to sign in to reddit and allow poste to grab data
            }
        });


        GetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = PosteApplication.getLoggedInUser(); //Calling the Login Email from the person who logged in in the Login Page

                //SendingData();//This should be passing the data we wish to post.
            }
        });
    }


    public void AuthorizationURL(String url)//Opens a url
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }



    public void SendingData(String authorization_code, String code, String redirect_uri)
    {
        //Creating the builder and passing the url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/api/v1/access_token")
                .build();

        RedditApi redditApi = retrofit.create(RedditApi.class);//Creating instance of API class

        //Passing our Data from above to our Data Class
        Data data = new Data(authorization_code, code, redirect_uri);

        //Calling a method to create a post and pass our data class
        Call<Data> call = redditApi.createPost(data);


        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Toast.makeText(redditInstructions.this, "Data added to API", Toast.LENGTH_SHORT).show();

                //We are getting response from our body
                //and passing it to our data class
                Data responseFromAPI = response.body();

                //Below we are getting our data from data class and adding it to our string
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getAuthorization_code() + "\n" + "code : " + responseFromAPI.getRedirect_uri();

                //Below we are setting our string to our textview

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

                Toast.makeText(redditInstructions.this, "Failed to connect", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void findUser()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("")
                .build();

        RedditApi redditApi = retrofit.create(RedditApi.class);

        //Passing our Data to the GetUser class
        GetUser getUser = new GetUser();

        //Call<GetUser> call = redditApi.createPost(getUser);






    }


}
