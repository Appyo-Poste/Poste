package com.example.poste.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
import retrofit2.http.Path;

public interface MyApiService {

    @POST("users/")
    Call<ResponseBody> registerUser(@Body RegisterRequest registerRequest);

    @POST("folders/")
    Call<ResponseBody> createFolder(@Body FolderRequest folderRequest);

    @GET("folders/folders/deleteFolder/{id}/")
    Call<ResponseBody> deleteFolder(@Path("id") int id);

}
