package com.example.poste.utils;

import android.util.Log;

import com.example.poste.models.Folder;
import com.example.poste.models.Post;
import com.example.poste.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * A utility class for debugging and logging.
 */
public class DebugUtils {

    /**
     * Logs the user details, including folders and posts, to the console. Note that for Android
     * Studio this will be the Logcat console.
     * Useful for dumping the user details to the console for debugging purposes.
     * @param user the user to log the details of.
     */
    public static void logUserFoldersAndPosts(User user) {
        if (user != null) {
            // Log user details
            Log.d("UserDebug", "User Details:");
            Log.d("UserDebug", "\tToken: " + user.getToken());

            List<Folder> folders = user.getFolders();

            if (folders != null) {
                for (int i = 0; i < folders.size(); i++) {
                    Folder folder = folders.get(i);

                    // Log the folder details
                    Log.d("UserDebug", "Folder " + (i + 1) + ":");
                    Log.d("UserDebug", "\tTitle: " + folder.getTitle());
                    Log.d("UserDebug", "\tID: " + folder.getId());
                    Log.d("UserDebug", "\tUser Permission: " + folder.getUserPermission());

                    List<Post> posts = folder.getPosts();

                    if (posts != null) {
                        for (int j = 0; j < posts.size(); j++) {
                            Post post = posts.get(j);

                            // Log the post details within the current folder
                            Log.d("UserDebug", "\tPost " + (j + 1) + ":");
                            Log.d("UserDebug", "\t\tTitle: " + post.getTitle());
                            Log.d("UserDebug", "\t\tID: " + post.getId());
                            Log.d("UserDebug", "\t\tDescription: " + post.getDescription());
                            Log.d("UserDebug", "\t\tURL: " + post.getUrl());
                        }
                    } else {
                        Log.d("UserDebug", "\tNo posts available in this folder.");
                    }
                }
            } else {
                Log.d("UserDebug", "User has no folders.");
            }
        } else {
            Log.d("UserDebug", "User object is null.");
        }
    }

    public static void logResponse(Response<ResponseBody> response) {
        Log.d("ResponseDebug", "Response code: " + response.code());
        Log.d("ResponseDebug", "Response message: " + response.message());
        Log.d("ResponseDebug", "Response body: " + response.body());
    }
}
