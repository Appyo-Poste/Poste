package com.example.poste;

import android.app.Application;
import android.content.Context;

import com.example.poste.api.poste.models.User;

public class PosteApplication extends Application {
    private static Context context;
    private static User currentUser;

    public void onCreate() {
        super.onCreate();
        PosteApplication.context = getApplicationContext();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        PosteApplication.currentUser = currentUser;
    }

    public static Context getAppContext() {
        return PosteApplication.context;
    }

    public static Application getApp() {
        return (Application) PosteApplication.context;
    }
}
