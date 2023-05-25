package com.example;

import android.app.Application;
import android.content.Context;

public class PosteApplication extends Application {
    private static Context context;
    private static String loggedInUser;

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
