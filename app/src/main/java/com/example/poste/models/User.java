package com.example.poste.models;

import com.example.poste.http.CreatePost;
import com.example.poste.http.DeletePost;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.utils.DebugUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents a User of the Poste app. Should closely mirror the Backend User model.
 */
public class User {

    /**
     * Singleton instance of the User class.
     */
    private static User user;

    /**
     * The user's token, used for authentication. This should be retrieved from a login response,
     * and sent with subsequent requests to validate the user to the backend. This way, we don't
     * need to send further user details -- the token is sufficient to identify the user.
     */
    private String token;

    /**
     * The user's folders. This should be retrieved from a data response, and should be updated
     * whenever the user's data is updated.
     */
    private final Collection<Folder> folders;

    /**
     * Private constructor for the User class. This should not be called directly -- instead, use
     * the {@link #getUser()} method to retrieve the singleton instance of the User class.
     */
    private User() {
        token = "";
        folders = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the User class. If the user has not yet been initialized,
     * this will initialize the user.
     * @return the singleton instance of the User class.
     */
    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }


    /**
     * Adds a folder to the user's folders.
     * @param newFolder the folder to add to the user's folders.
     */
    public void addFolder(Folder newFolder) {
        folders.add(newFolder);
    }

    /**
     * Returns a copy of the list of the user's folders. This is a copy, so modifying the returned
     * list will not modify the user's folders.
     * @return a copy of the list of the user's folders.
     */
    public List<Folder> getFolders() {
        return new ArrayList<>(folders);
    }

    /**
     * Returns the user's token. Note that this method only retrieves the previously-set token, and
     * does not make any API calls to retrieve the token.
     *
     * @return the user's token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the user's token, prepended with "Token " for authentication.
     * For example, if the token is "A12345", this will return "Token 12345"
     * This is the expected format for the Authorization header, which will look like:
     * "Authorization": "Token A12345"
     * @return the user's token, prepended with "Token "
     */
    private String getTokenHeader() {
        return "Token " + getToken();
    }
    /**
     * Sets the user's token. This should be retrieved from a login response, and sent with
     * subsequent requests to validate the user to the backend. This way, we don't need to send
     * further user details -- the token is sufficient to identify the user.
     *
     * @param token the user's token.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Update user's folders and posts from API. Naive implementation that simply adds all folders
     * and posts to the user's folders and posts. This serves as a full refresh of the user's data,
     * and could be improved to only add/remove folders and posts that have changed at some point.
     */
    public void updateFoldersAndPosts(Context context) {
        MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
        Call<ResponseBody> call = apiService.getData(getTokenHeader());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    // convert response to json obj
                    try {
                        String jsonResponse = response.body().string();
                        Log.d("Response", jsonResponse);
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject folder = jsonArray.getJSONObject(i);
                            String folderTitle = folder.getString("title");
                            String folderUserPermission = folder.getString("user_permission");
                            String folderId = folder.getString("id");
                            List<Post> posts = new ArrayList<>();
                            JSONArray postsArray = folder.getJSONArray("posts");
                            for (int j = 0; j < postsArray.length(); j++) {
                                JSONObject post = postsArray.getJSONObject(j);
                                String postTitle = post.getString("title");
                                String postDescription = post.getString("description");
                                String postUrl = post.getString("url");
                                String postId = post.getString("id");
                                Post newPost = new Post.Builder()
                                        .setTitle(postTitle)
                                        .setDescription(postDescription)
                                        .setUrl(postUrl)
                                        .setId(postId)
                                        .build();
                                posts.add(newPost);
                            }
                            Folder newFolder = new Folder.Builder()
                                    .setTitle(folderTitle)
                                    .setUserPermission(folderUserPermission)
                                    .setPosts(posts)
                                    .setId(folderId)
                                    .build();
                            user.addFolder(newFolder);
                        }
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("UserDebug", "Retrieved user data from API from User.updateFoldersAndPosts()");
                    DebugUtils.logUserFoldersAndPosts(user);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String display = call.toString();
                Toast.makeText(context, display, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Parses the error response from the backend. This function expects the error response in the
     * following JSON format:
     * {
     *    "result": {
     *      "success": false,
     *      "errors": {
     *          "key": ["error message 1", "error message 2", ...]
     *      }
     *    }
     * }
     *
     * The function extracts the first error message associated with the first key within the
     * "errors" object.
     * It cleans the error message by removing any instances of the following characters:
     * "[", "]", and "'".
     *
     * If the "success" field is true, if there are no errors, or if an exception occurs during the
     * parsing, the function will return null.
     *
     * @param response The response to parse for error messages.
     * @return A string containing the key and its associated cleaned-up error message,
     *         or null if there is no error message or if parsing fails.
     */
    private String parseError(Response<ResponseBody> response) {
        try {
            // Parse the error response
            String errorBody = response.errorBody().string();
            JSONObject json = new JSONObject(errorBody);
            JSONObject result = json.getJSONObject("result");

            boolean success = result.getBoolean("success");

            if (!success && result.has("errors")) {
                JSONObject errors = result.getJSONObject("errors");
                // Assuming the errors object has keys with error messages
                // Grab first error message
                Iterator<String> keys = errors.keys();
                if (keys.hasNext()) {
                    String firstErrorKey = keys.next();

                    JSONArray errorArray = errors.getJSONArray(firstErrorKey);
                    if (errorArray.length() > 0) {
                        String rawErrorMessage = errorArray.getString(0);

                        // Remove unwanted characters
                        String cleanedErrorMessage = rawErrorMessage.replace("[", "").replace("]", "").replace("'", "");

                        return firstErrorKey + ": " + cleanedErrorMessage;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Adds a new post the given folder
     *
     * @param folder folder to add the new post to
     */
    public void createNewPost(Folder folder, Context context, String title, String description, String url) {
        MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
        Call<ResponseBody> call = apiService.createPost(getTokenHeader() ,new CreatePost(title, description, url, folder.getTitle()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    updateFoldersAndPosts(context);
                }
                else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Server Not Responding", Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * Deletes a given post from a given folder in the data model and backend
     *
     * @param post the post you want to delete
     */
    public void deletePost(Context context, Post post, Folder folder) {
        MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
        Call<ResponseBody> call = apiService.deletePost(getTokenHeader() ,new DeletePost(post.getId()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    updateFoldersAndPosts(context);
                }
                else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Server Not Responding", Toast.LENGTH_LONG);
            }
        });
    }
}
