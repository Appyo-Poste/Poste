/**
 * modified the Application so that you can get to the resources folder without passing context
 */
package com.example.poste.models;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class PosteApp extends Application {
    private static Context context;

    public static Resources getResourcesStatic() {
        return context.getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = getApplicationContext();
    }
}
