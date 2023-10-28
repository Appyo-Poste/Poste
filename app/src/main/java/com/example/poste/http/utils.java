package com.example.poste.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class utils {

    /**
     * Parses an error response from the backend. This function expects the error response in the
     * following JSON format:
     * <pre>
     * {@code
     *   {
     *     "result": {
     *       "success": false,
     *       "errors": {
     *         "key": ["error message 1", "error message 2", ...]
     *       }
     *     }
     *   }
     * }
     * </pre>
     * The function extracts the first error message associated with the first key within the
     * "errors" object.
     * It cleans the error message by removing any instances of the following characters:
     * "[", "]", and "'".
     * <br>
     * If the "success" field is true, if there are no errors, or if an exception occurs during the
     * parsing, the function will return null.
     *
     * @param response The response to parse for error messages.
     * @return A string containing the key and its associated cleaned-up error message,
     *         or null if there is no error message or if parsing fails.
     */
    public static String parseError(Response<ResponseBody> response) {
        try {
            // Parse the error response
            String errorBody = response.errorBody().string();
            JSONObject json = new JSONObject(errorBody);
            JSONObject result = json.getJSONObject("result");

            boolean success = result.getBoolean("success");

            if (!success && result.has("errors")) {
                JSONObject errors = result.getJSONObject("errors");
                // Assuming the errors object has keys with error messages
                // Grab first error message
                Iterator<String> keys = errors.keys();
                if (keys.hasNext()) {
                    String firstErrorKey = keys.next();

                    JSONArray errorArray = errors.getJSONArray(firstErrorKey);
                    if (errorArray.length() > 0) {
                        String rawErrorMessage = errorArray.getString(0);

                        // Remove unwanted characters
                        String cleanedErrorMessage = rawErrorMessage
                                .replace("[", "")
                                .replace("]", "")
                                .replace("'", "");

                        return firstErrorKey + ": " + cleanedErrorMessage;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
