package com.example.poste.api.poste;

import android.util.Log;

import com.example.poste.api.poste.exceptions.EmailAlreadyUsedException;
import com.example.poste.api.poste.exceptions.IncompleteRequestException;
import com.example.poste.api.poste.exceptions.MalformedResponseException;
import com.example.poste.api.poste.exceptions.NoUserFoundException;
import com.example.poste.api.poste.exceptions.UserCreationException;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.models.Post;
import com.example.poste.models.User;
import com.example.poste.utils.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Documentation by Jacob Paulin and Michael Khmourovitch.
 *
 * For all intents and purposes this is the most important Java class in this project.
 *
 * As the name implies, this class is responsible for performing HTTP requests to the Poste URL,
 * which is essentially the backbone of the entire application.
 *
 * Existing documentation for endpoints by Jacob Paulin is available on Github.
 */
public class API {

    /**
     * Base URL for API
     * Currently, this is configured to use PosteBackend API Django Project when run locally
     * See https://github.com/Appyo-Poste/PosteBackend for more details
     * Note: While the PosteBackend Django project runs locally at localhost:8000, Android Studio
     * uses the special URL "10.0.2.2" to access something running locally on the same host.
     */
    private static final String HOST_URL = "http://10.0.2.2:8000/api";

    /**
     * Constructor for the URL object, uses the host URL and attaches the given endpoint.
     *
     * @param endpoint that gets appended to the URL string.
     * @return URL Object that is properly formatted if successful.
     *         If a MalformedURLException occurs, return null instead of a broken link.
     */
    public static URL URL(String endpoint) {
        try {
            return new URL(String.join("", HOST_URL, endpoint));
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a given HTTP request. Takes in a request object, and returns a response.
     * Note: This should be performed on a separate thread. An example exists in
     * RegisterActivity.AsyncLogin.doInBackground().
     *
     * @param request HTTP Request object
     * @return response object for the given request.
     *         Returns null by default if an exception occurs.
     */
    private static final Response performHttpRequest(Request request) {
        Response result = null;
        HTTPRequest httpRequest = new HTTPRequest();

        httpRequest.execute(request);

        try {
            result = httpRequest.get();
        } catch (Exception exception) {}

        return result;
    }

    /**
     * Attempts to login. Returns User object if successful, else null.
     * @param email email address to login
     * @param password password to authenticate
     * @return User if validated credentials, else null
     * @throws MalformedResponseException
     * @throws IncompleteRequestException
     */
    public static User login(String email, String password) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointUsersLogin(email, password)) {
            if (response.body() == null) {
                throw new MalformedResponseException();
            }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");
            if (responseJson.getBoolean("success")) {
                // request worked; create a user
                JSONObject userJson = responseJson.getJSONObject("user");
                int id = userJson.getInt("id");
                String username = userJson.getString("username");
                return User.getUser();
            } else {
                return null;
            }
        } catch (JSONException e) {
            throw new MalformedResponseException();
        } catch (IOException e) {
            throw new IncompleteRequestException();
        }
    }

    /**
     * Attempts to create a User via API call. Returns true if successful, else false.
     * Called by RegisterActivity.AsyncLogin.doInBackground() when user clicks register button.
     * @param email email to use for new user
     * @param name name to use for new user
     * @param password password to use for new user
     * @return true if successful, else false
     * @throws MalformedResponseException if response was malformed
     * @throws IncompleteRequestException if incomplete request provided
     * @throws EmailAlreadyUsedException if email already in use
     */
    public static boolean addUser(String email, String name, String password) throws MalformedResponseException, IncompleteRequestException, EmailAlreadyUsedException, UserCreationException {
        try (Response response = endpointUsersAdd(email, name, password)) {
            if (response.body() == null) {
                throw new MalformedResponseException();
            }
            // check if 201 (succeeded) or 400 (failed)
            if (!response.isSuccessful()){
                JSONObject responseJson = new JSONObject(response.body().string());
                if (responseJson.has("email")){
                    String error = responseJson.getJSONArray("email").getString(0);
                    throw new UserCreationException(error);
                } else if (responseJson.has("name")){
                    String error = responseJson.getJSONArray("name").getString(0);
                    throw new UserCreationException(error);
                } else if (responseJson.has("password")){
                    String error = responseJson.getJSONArray("password").getString(0);
                    throw new UserCreationException(error);
                } else {
                    throw new UserCreationException();
                }
            } else {
                return true;
            }
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean updateUser(String email, String username, String password) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointUsersUpdate(email, username, password)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean deleteUser(String email, String password) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointUsersDelete(email, password)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static int addPost(String name, String link, int ownerId) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointPostsAdd(name, link, ownerId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getInt("newPostId");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean updatePost(int id, String name, String link, int ownerId) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointPostsUpdate(id, name, link, ownerId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean deletePost(int id) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointPostsDelete(id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static HashMap<Integer, FolderAccess> getAccessForFolderId(int id) throws MalformedResponseException, IncompleteRequestException {
        HashMap<Integer, FolderAccess> access = new HashMap<>();

        try (Response response = endpointFoldersUsers(id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {
                    JSONObject obj = responseJson.getJSONObject(i);

                    int userId = obj.getInt("userId");
                    int _access = obj.getInt("access");

                    access.put(userId, FolderAccess.valueOf(_access));
                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return access;
    }

    public static int addFolder(String name, int ownerId) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointFoldersAdd(name, ownerId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getInt("newFolderId");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean updateFolder(int folderId, String name, int ownerId) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointFoldersUpdate(folderId, name, ownerId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean addPostToFolder(int postId, int folderId) throws MalformedResponseException, IncompleteRequestException{
        try (Response response = endpointFoldersPostsAdd(folderId, postId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean removePostFromFolder(int postId, int folderId) throws MalformedResponseException, IncompleteRequestException{
        try (Response response = endpointFoldersPostsDelete(folderId, postId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean addUserToFolder(int userId, int folderId, FolderAccess access) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointFoldersUsersAdd(folderId, userId, access)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");
            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean updateUserAccessToFolder(int userId, int folderId, FolderAccess access) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointFoldersUsersUpdate(userId, folderId, access)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");
            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean removeUserFromFolder(int userId, int folderId) throws MalformedResponseException, IncompleteRequestException{

        try (Response response = endpointFoldersUsersDelete(userId, folderId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }



    private static Response endpointUsers() throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/users")))
                .build());
    }

    private static Response endpointUsersId(String id) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/users/id/%s", id))))
                .build());
    }

    private static Response endpointUsersEmail(String email) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/users/email/%s", email))))
                .build());
    }

    private static Response endpointUsersLogin(String email, String password) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException exception) {
            System.out.println("Something happened");
        }
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString()
        );
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/login/")))
                .post(body)
                .build();
        return(performHttpRequest(request));
    }

    private static Response endpointUsersAdd(String email, String name, String password) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("name", name);
        } catch (JSONException exception) {
            System.out.println("Something happened");
        }
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString()
        );
        String endpoint = "/users/";
        Log.d("API", "URL: " + URL(endpoint));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL(endpoint)))
                .post(body)
                .build();
        return(performHttpRequest(request));
    }

    private static Response endpointUsersUpdate(String email, String username, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("email=%s&username=%s&password=%s", email, username, password));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/users/update")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointUsersDelete(String email, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("email=%s&password=%s", email, password));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/users/delete")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointPosts() throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts")))
                .build());
    }

    private static Response endpointPostsId(int id) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/posts/id/%s", id))))
                .build());
    }

    private static Response endpointPostsUser(int id) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/posts/user/%s", id))))
                .build());
    }

    private static Response endpointPostsAdd(String name, String link, int ownerId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("name=%s&link=%s&ownerId=%d", name, link, ownerId));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointPostsUpdate(int id, String name, String link, int ownerId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("id=%s&name=%s&link=%s&ownerId=%d", id, name, link, ownerId));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts/update")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointPostsDelete(int id) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("id=%d", id));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts/delete")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFolders() throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders")))
                .build());
    }

    private static Response endpointFoldersId(int id) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/folders/id/%d", id))))
                .build());
    }

    private static Response endpointFoldersUser(int id) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/folders/user/%d", id))))
                .build());
    }

    private static Response endpointFoldersPosts(int id) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/folders/posts/%d", id))))
                .build());
    }

    private static Response endpointFoldersUsers(int id) throws IOException {
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/folders/users/%d", id))))
                .build());
    }

    private static Response endpointFoldersAdd(String name, int ownerId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("name=%s&ownerId=%d", name, ownerId));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFoldersUpdate(int id, String name, int ownerId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("id=%s&name=%s&ownerId=%d", id, name, ownerId));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/update")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFoldersDelete(int id) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("id=%d", id));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/delete")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFoldersPostsAdd(int folderId, int postId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("folderId=%d&postId=%d", folderId, postId));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/posts/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFoldersPostsDelete(int folderId, int postId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("folderId=%d&postId=%d", folderId, postId));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/posts/delete")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFoldersUsersAdd(int folderId, int userId, FolderAccess access) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("folderId=%d&userId=%d&access=%d", folderId, userId, access.getValue()));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/users/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFoldersUsersUpdate(int folderId, int userId, FolderAccess access) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("folderId=%d&userId=%d&access=%d", folderId, userId, access.getValue()));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/users/update")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }

    private static Response endpointFoldersUsersDelete(int folderId, int userId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("folderId=%d&userId=%d", folderId, userId));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/users/delete")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
    }
}

