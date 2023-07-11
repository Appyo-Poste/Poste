package com.example.legacy;

import android.app.Application;
import android.content.Context;

import com.example.legacy.poste.api.models.User;

public class PosteApplication extends Application {
    private static Context context;
    private static String loggedInUser;
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        PosteApplication.currentUser = currentUser;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(String loggedInUser) {
        PosteApplication.loggedInUser = loggedInUser;
    }

    public void onCreate() {
        super.onCreate();
        PosteApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return PosteApplication.context;
    }
    public static Application getApp() {
        return (Application) PosteApplication.context;
    }
}
