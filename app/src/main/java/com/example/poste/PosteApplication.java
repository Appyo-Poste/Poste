package com.example.poste;

import android.app.Application;

import com.example.poste.models.User;
import com.example.poste.models.Folder;
import com.example.poste.models.Post;

/**
 * A custom application class that extends the Android Application class
 * It is used to maintain a global application context and
 * store information about the currently logged-in user
 */
public class PosteApplication extends Application {
    // This needs to get refactored out of the code
    private static User currentUser;
    private static Folder selectedFolder;
    private static Post selectedPost;

    /**
     * Called when the application is starting
     *
     * This is where we initialize any data
     * that will be used across the entire application
     */
    public void onCreate() {
        super.onCreate();
        // Initialize the application context
    }

    /**
     * Get the currently logged-in user
     *
     * @return The User object representing the currently logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the currently logged-in user
     *
     * @param currentUser The User object representing the currently logged-in user
     */
    public static void setCurrentUser(User currentUser) {
        PosteApplication.currentUser = currentUser;
    }

    /**
     * Get currently selected folder
     *
     * @return the currently selected folder
     */
    public static Folder getSelectedFolder() {return selectedFolder; }

    /**
     * Sets which folder is currently selected
     *
     * @param folder the folder that we want to select
     */
    public static void setSelectedFolder(Folder folder) {
        selectedFolder = folder;
    }

    /**
     * Get currently selected post
     *
     * @return the currently selected post
     */
    public static Post getSelectedPost() {return selectedPost; }

    /**
     * Sets which post is currently selected
     *
     * @param post the post that we want to select
     */
    public static void setSelectedPost(Post post) {
        selectedPost = post;
    }
}
