package com.example.poste.utils;

import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTPRequest extends AsyncTask<Request, Integer, Response> {
    private static final String TAG = "HTTPRequest";

    /**
     * Mechanics of the class, this is where we code our logic for the class
     * @param requests arguments to use when making a http request
     * @return The result from the API
     */
    @Override
    protected Response doInBackground(Request... requests) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        try {

            return client.newCall(requests[0]).execute();

        } catch (Exception exception) { exception.printStackTrace(); }

        return null;
    }

    /**
     * Overriding method from parent class
     * @param args
     */
    public void onProgressUpdate(Integer ... args) {
        Log.i(TAG, "onProgressUpdate");
    }

    /**
     * Method which is executed once the task is complete
     * @param response
     */
    public void onPostExecute(Response response) {
        super.onPostExecute(response);
        Log.i(TAG, "onPostExecute");
    }
}