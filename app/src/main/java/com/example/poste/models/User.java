package com.example.poste.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.poste.callbacks.PostDeletionCallback;
import com.example.poste.callbacks.UpdateCallback;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.utils.DebugUtils;
import com.example.poste.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents a User of the Poste app. Should closely mirror the Backend User model.
 */
public class User {

    /**
     * Sets the currently active Folder
     */
    private Folder selectedFolder;

    /**
     * Sets the currently active Post
     */
    private Post selectedPost;

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
    private final List<Folder> folders;

    /**
     * The email used for the user's account
     */
    private String email;

    /**
     * The user's first name
     */
    private String firstName;

    /**
     * The user's last name
     */
    private String lastName;

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
     * sets the user's email
     * @param email String containing user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get a folder based on the string folder ID, or null if not found
     *
     * @param folderId string containing the id of the folder
     * @return Folder if found, else null
     */
    public Folder getFolder(String folderId) {
        for (Folder folder :
                user.folders) {
            if (folder.getId().equals(folderId)) {
                return folder;
            }
        }
        return null;
    }

    /**
     * Get a post based on the string post ID, or null if not found
     *
     * @param postId string containing the id of the post
     * @return Post if found, else null
     */
    public Post getPost(String postId) {
        for (Folder folder :
                user.folders) {
            for (Post post:
                folder.getPosts()){
                if (post.getId().equals(postId)){
                    return post;
                }
            }
        }
        return null;
    }

    /**
     * Returns List of the user's folders.
     * @return a List of the user's folders.
     */
    public List<Folder> getFolders() {
        return folders;
    }

    /**
     * Returns the user's token. Note that this method only retrieves the previously-set token, and
     * does not make any API calls to retrieve the token. If you want to populate a user's token in
     * a request header, use {@link #getTokenHeaderString()} instead.
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
    public String getTokenHeaderString() {
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
    public void updateFoldersAndPosts(UpdateCallback callback) {
        MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
        Call<ResponseBody> call = apiService.getData(getTokenHeaderString());
        List<Folder> backup = new ArrayList<>(user.getFolders());
        List<Folder> newFolders = new ArrayList<>();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        Log.d("Response", jsonResponse);
                        // user.folders.clear();
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
                            newFolders.add(newFolder);
                        }
                        user.folders.clear();
                        user.folders.addAll(newFolders);
                        Log.d(
                                "UserDebug",
                                "Retrieved user data from API from User.updateFoldersAndPosts()"
                        );
                        DebugUtils.logUserFoldersAndPosts(user);
                        callback.onSuccess();
                    } catch (JSONException | IOException e) {
                        user.folders.clear();
                        user.folders.addAll(backup);
                        Log.e(
                                "UserDebug",
                                "Error parsing JSON response from API in " +
                                        "User.updateFoldersAndPosts(): ", e
                        );
                        callback.onError("Error parsing JSON response from API");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(
                        "UserDebug",
                        "Error retrieving user data from API in User.updateFoldersAndPosts(), " +
                                "no response received: ", t);
                user.folders.clear();
                user.folders.addAll(backup);
                callback.onError("Unable to retrieve folders and posts, please try " +
                        "again");
            }
        });
    }

    /**
     * Deletes a post from the backend API.
     *
     * This method uses the {@link PostDeletionCallback} interface to handle the results of the
     * asynchronous API call. This interface defines two methods:
     * {@link PostDeletionCallback#onSuccess()} and {@link PostDeletionCallback#onError(String)}.
     *
     * By implementing this interface, you can define the desired actions based on the deletion
     * outcome by overriding these methods. For example, if this is called in an Activity, you can
     * display a Toast message in the {@link PostDeletionCallback#onSuccess()} method to notify the
     * user that the post was successfully deleted, or display a Toast message in the
     * {@link PostDeletionCallback#onError(String)} method to notify the user that the post could
     * not be deleted.
     *
     * Example of creating and passing a callback, for instance in an Activity:
     *
     * <pre>
     * {@code
     * PostDeletionCallback callback = new PostDeletionCallback() {
     *     @Override
     *     public void onSuccess() {
     *         // e.g. Display a Toast message for successful deletion. What you want to happen
     *         // after a successful deletion should be defined here. Note that this method
     *         // already removes the post from the user's folders, so you don't need to do that
     *         // here.
     *     }
     *
     *     @Override
     *     public void onError(String errorMessage) {
     *         // e.g. Display a Toast message for errors. What you want to happen after an error
     *         // should be defined here.
     *     }
     * };
     *
     * User user = User.getUser();
     * user.deletePost(postToBeDeleted, callback);
     * }
     * </pre>
     *
     * @param post The Post model to delete.
     * @param callback The callback to be invoked after attempting to delete the post.
     */
    public void deletePostFromServer(Post post, PostDeletionCallback callback) {
        String id = post.getId();
        Log.d("UserDebug", "Attempting to delete post ID " + id);
        if (id == null) {
            callback.onError("Post ID is null");
            Log.d("UserDebug", "Error deleting post: post ID is null");
            return;
        }
        
        MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
        Log.d("UserDebug", "Sending delete request to API for Post ID " + id);
        Call<ResponseBody> call = apiService.deletePost(getTokenHeaderString(), id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    removePostFromLocalFolders(post);
                    callback.onSuccess(); // Notify callback of success.
                } else {
                    String error = utils.parseError(response);
                    // The request successfully reached the backend API, but the API returned an
                    // error.
                    // This passes back the error message from the backend API, if it exists, or a
                    // generic error message if it doesn't.
                    callback.onError(error != null ? error : "Error deleting post.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // The request failed to reach the backend API altogether.
                callback.onError("Unable to contact server to delete post.");
            }
        });
    }

    private void removePostFromLocalFolders(Post post) {
        for (Folder folder : folders) {
            List<Post> posts = folder.getPosts();
            if (posts != null) {
                for (Post p : posts) {
                    if (p.getId().equals(post.getId())) {
                        posts.remove(p);
                        return;
                    }
                }
            }
        }
    }

    public boolean isLoggedIn() {
        return (token != null && !token.isEmpty());
    }

    public void logout() {
        user = null;
    }

    public void setSelectedFolder(Folder folder) {
        this.selectedFolder = folder;

    }

    public void setSelectedPost(Post post) {
        this.selectedPost = post;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public Post getSelectedPost() {
        return selectedPost;
    }
}
