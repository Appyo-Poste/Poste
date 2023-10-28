package com.example.poste.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
import retrofit2.http.Path;

public interface MyApiService {

    @POST("posts/")
    Call<ResponseBody> createPost(
            @Header("Authorization") String authToken,
            @Body CreatePost createPost
    );

    @PATCH("posts/{id}/")
    Call<ResponseBody> editPost(
            @Header("Authorization") String authToken,
            @Path("id") String id,
            @Body EditPostRequest editPostRequest
    );

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
    Call<ResponseBody> getData(
            @Header("Authorization") String authToken
    );

    @POST("folders/")
    Call<ResponseBody> createFolder(
            @Header("Authorization") String authToken,
            @Body FolderRequest folderRequest
    );

    @GET("folders/folders/deleteFolder/{id}/")
    Call<ResponseBody> deleteFolder(
            @Header("Authorization") String authToken,
            @Path("id") String id
    );

    @POST("login/")
    Call<ResponseBody> loginUser(
            @Body LoginRequest loginRequest
    );

    @POST("posts/")
    Call<ResponseBody> createPost(
            @Header("Authorization") String authToken,
            @Body PostRequest postRequest
    );

    /**
     * Delete a post from the backend API. Note that this API call expects the User's token to be
     * sent in the request header. The header should be formatted as follows:<br>
     * <pre>
     * "Authorization": "Token abcd1234"
     * </pre>
     * <br>
     * It also expects the ID of the post to be deleted to be sent in the header.
     * The header should be formatted as follows:
     * <pre>
     * "id": "value"
     * </pre>
     * <br>
     * To do this, we use the {@code @Header} annotation. For more information, see:
     * <ul>
     *     <li><a href="https://square.github.io/retrofit/2.x/retrofit/index.html?retrofit2/http/Header.html">Retrofit Headers</a></li>
     * </ul>
     *
     * @param authToken Authorization token in the format "Token &lt;token&gt;"
     * @param id ID of the post to be deleted in the format "id: &lt;id&gt;"
     */
    @DELETE("posts/{id}/")
    Call<ResponseBody> deletePost(
            @Header("Authorization") String authToken,
            @Path("id") String id
    );

    @POST("users/")
    Call<ResponseBody> registerUser(
            @Body RegisterRequest registerRequest
    );
}
