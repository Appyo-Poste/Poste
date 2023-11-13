package com.example.poste.utils;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The HTTPRequest class is an asynchronous task that facilitates
 * making HTTP requests using OkHttp library in the background
 *
 * It extends the AsyncTask class and performs the network operations
 * on a separate thread to avoid crashing the application
 */
public class HTTPRequest extends AsyncTask<Request, Integer, Response> {
    private static final String TAG = "HTTPRequest";

    /**
     * This method is executed on a background thread and performs the actual HTTP request.
     *
     * @param requests Arguments to use when making a HTTP request
     * @return The result of the HTTP request, which is an HTTP response object.
     */
    @Override
    protected Response doInBackground(Request... requests) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        try {
            // Execute the HTTP request using OkHttp client and return the response.
            return client.newCall(requests[0]).execute();
        } catch (Exception exception) { exception.printStackTrace(); }

        return null;
    }

    /**
     * This method is called on the UI thread when there's
     * progress to report during the background computation
     *
     * @param args Progress arguments
     */
    public void onProgressUpdate(Integer ... args) {
        Log.i(TAG, "onProgressUpdate");
    }

    /**
     * This method is executed on the UI thread after the doInBackground method completes
     * It receives the result of the HTTP request
     *
     * @param response The HTTP response received from the server
     */
    public void onPostExecute(Response response) {
        super.onPostExecute(response);
        Log.i(TAG, "onPostExecute");
    }
}