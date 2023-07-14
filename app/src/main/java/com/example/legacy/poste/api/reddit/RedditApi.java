package com.example.legacy.poste.api.reddit;

import com.example.legacy.poste.api.reddit.Data;
import com.example.legacy.poste.api.reddit.GetUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RedditApi{ //Call object to the api



    @POST("") //Sending for the barrer token
    Call<com.example.legacy.poste.api.reddit.Data> createPost(@Body Data data);


    @GET("/user{userName}/saved")//The API END POINT
    Call<GetUser> findUser(@Header("Dynamic Header") String Header,
                           @Path("userName") String userName); //Sets a dynamic path
}
