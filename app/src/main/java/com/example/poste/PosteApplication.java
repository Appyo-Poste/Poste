package com.example.poste;

import android.app.Application;
import android.content.Context;

import com.example.poste.api.poste.models.User;

/**
 * A custom application class that extends the Android Application class
 * It is used to maintain a global application context and
 * store information about the currently logged-in user
 */
public class PosteApplication extends Application {
    private static Context context;
    private static User currentUser;

    /**
     * Called when the application is starting
     *
     * This is where we initialize any data
     * that will be used across the entire application
     */
    public void onCreate() {
        super.onCreate();
        // Initialize the application context
        PosteApplication.context = getApplicationContext();
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
     * Get the application's context
     *
     * @return The application's context
     */
    public static Context getAppContext() {
        return PosteApplication.context;
    }

    /**
     * Get the instance of the application
     *
     * @return The application instance as an `Application` object.
     */
    public static Application getApp() {
        return (Application) PosteApplication.context;
    }
}
