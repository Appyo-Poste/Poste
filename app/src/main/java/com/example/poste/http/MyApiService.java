package com.example.poste.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import okhttp3.ResponseBody;

public interface MyApiService {

    @POST("users/")
    Call<ResponseBody> registerUser(@Body RegisterRequest registerRequest);

    @POST("login/")
    Call<ResponseBody> loginUser(@Body LoginRequest loginRequest);


    /**
     * Get data from API. Used for retrieving folders and posts. Note that this API call expects
     * the User's token to be sent in the request header. The header should be formatted as follows:
     * "Authorization": "Token <token>"
     * To do this, we use the @Header annotation. For more information, see:
     * <a href="https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Header.html">Retrofit Headers</a>
     *
     * @param authToken Authorization token, to be sent in the header, in the format "Token <token>"
     * @return ResponseBody object containing the response from the API
     */
    @GET("data/")
    Call<ResponseBody> getData(@Header("Authorization") String authToken);
}
