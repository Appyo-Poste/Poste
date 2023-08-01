package com.example.poste.api.poste;

import android.util.Log;

import com.example.poste.api.poste.exceptions.EmailAlreadyUsedException;
import com.example.poste.api.poste.exceptions.IncompleteRequestException;
import com.example.poste.api.poste.exceptions.MalformedResponseException;
import com.example.poste.api.poste.exceptions.NoUserFoundException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.Post;
import com.example.poste.api.poste.models.User;
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
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* *******JAVA API CLASS*******

Documentation by Jacob Paulin and Michael Khmourovitch.

For all intents and purposes this is the most important Java class in this project.

As the name implies, this class is responsible for performing HTTP requests to the Poste URL,
which is essentially the backbone of the entire application.

Existing documentation for endpoints by Jacob Paulin is available on Github.

*/
public class API {

    // This is the host URL for the API, will change as the app switches owners.
    private static final String HOST_URL = "https://poste-388415.uc.r.appspot.com/";

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

    public static ArrayList<User> getAllUsers() throws MalformedResponseException, IncompleteRequestException {
        ArrayList<User> users = new ArrayList<>();

        try (Response response = endpointUsers()) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {
                    JSONObject obj = responseJson.getJSONObject(i);

                    int id = obj.getInt("id");
                    String email = obj.getString("email");
                    String username = obj.getString("username");
                    String password = obj.getString("password");

                    users.add(new User(id, email, username, password));
                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return users;
    }

    public static User getUserById(String _id) throws MalformedResponseException, IncompleteRequestException, NoUserFoundException {
        try (Response response = endpointUsersId(_id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject resultJson = new JSONObject(response.body().string());

            if (resultJson.get("result") instanceof String) { throw new NoUserFoundException("Could not find user with the provided id"); }

            JSONObject responseJson = resultJson.getJSONObject("result");

            int id = responseJson.getInt("id");
            String email = responseJson.getString("email");
            String username = responseJson.getString("username");
            String password = responseJson.getString("password");

            return new User(id, email, username, password);
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static User getUserByEmail(String _email) throws MalformedResponseException, IncompleteRequestException, NoUserFoundException {
        try (Response response = endpointUsersEmail(_email)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject resultJson = new JSONObject(response.body().string());

            if (resultJson.get("result") instanceof String) { throw new NoUserFoundException("Could not find user with the provided email"); }

            JSONObject responseJson = resultJson.getJSONObject("result");

            int id = responseJson.getInt("id");
            String email = responseJson.getString("email");
            String username = responseJson.getString("username");
            String password = responseJson.getString("password");

            return new User(id, email, username, password);
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean validateUserLogin(String email, String password) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointUsersLogin(email, password)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static boolean addUser(String email, String username, String password) throws MalformedResponseException, IncompleteRequestException, EmailAlreadyUsedException {
        try (Response response = endpointUsersAdd(email, username, password)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            if (responseJson.getString("message").equals("Email already in use")) { throw new EmailAlreadyUsedException(); }

            return responseJson.getBoolean("success");
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

    public static ArrayList<Post> getAllPosts() throws MalformedResponseException, IncompleteRequestException {
        ArrayList<Post> posts = new ArrayList<>();

        try (Response response = endpointPosts()) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {
                    JSONObject obj = responseJson.getJSONObject(i);

                    int id = obj.getInt("id");
                    String name = obj.getString("name");
                    String link = obj.getString("link");
                    int ownerId = obj.getInt("ownerId");

                    posts.add(new Post(id, name, link, ownerId));
                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return posts;
    }

    public static Post getPostById(int _id) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointPostsId(_id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            int id = responseJson.getInt("id");
            String name = responseJson.getString("name");
            String link = responseJson.getString("link");
            int ownerId = responseJson.getInt("ownerId");

            return new Post(id, name, link, ownerId);
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static ArrayList<Post> getPostsForUserId(int _id) throws MalformedResponseException, IncompleteRequestException {
        ArrayList<Post> posts = new ArrayList<>();

        try (Response response = endpointPostsUser(_id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {
                    JSONObject obj = responseJson.getJSONObject(i);

                    int id = obj.getInt("id");
                    String name = obj.getString("name");
                    String link = obj.getString("link");
                    int ownerId = obj.getInt("ownerId");

                    posts.add(new Post(id, name, link, ownerId));
                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return posts;
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

    public static ArrayList<Folder> getAllFolders() throws MalformedResponseException, IncompleteRequestException{
        ArrayList<Folder> folders = new ArrayList<>();

        try (Response response = endpointFolders()) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {
                    JSONObject obj = responseJson.getJSONObject(i);

                    int id = obj.getInt("id");
                    String name = obj.getString("name");
                    int ownerId = obj.getInt("ownerId");


                    folders.add(new Folder(id, name, ownerId, getPostsForFolderId(id), getAccessForFolderId(id)));

                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return folders;
    }

    public static Folder getFolderById(int _id) throws MalformedResponseException, IncompleteRequestException{
        try (Response response = endpointFoldersId(_id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            int id = responseJson.getInt("id");
            String name = responseJson.getString("name");
            int ownerId = responseJson.getInt("ownerId");


            return new Folder(id, name, ownerId, getPostsForFolderId(_id), getAccessForFolderId(_id));
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    public static HashMap<Folder, FolderAccess> getFoldersForUserId(int id) throws MalformedResponseException, IncompleteRequestException {
        HashMap<Folder, FolderAccess> folders = new HashMap<>();

        try (Response response = endpointFoldersUser(id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {
                    JSONObject obj = responseJson.getJSONObject(i);

                    int folderId = obj.getInt("id");
                    String name = obj.getString("name");
                    int ownerId = obj.getInt("ownerId");
                    int access = obj.getInt("access");

                    folders.put(new Folder(folderId, name, ownerId, getPostsForFolderId(folderId), getAccessForFolderId(folderId)), FolderAccess.valueOf(access));
                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return folders;
    }

    public static ArrayList<Post> getPostsForFolderId(int id) throws MalformedResponseException, IncompleteRequestException {
        ArrayList<Post> posts = new ArrayList<>();

        try (Response response = endpointFoldersPosts(id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {
                    JSONObject obj = responseJson.getJSONObject(i);

                    int postId = obj.getInt("id");
                    String name = obj.getString("name");
                    String link = obj.getString("link");
                    int ownerId = obj.getInt("ownerId");

                    posts.add(new Post(postId, name, link, ownerId));
                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return posts;
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

    public static boolean deleteFolder(int folderId) throws MalformedResponseException, IncompleteRequestException {
        ArrayList<Post> posts = getPostsForFolderId(folderId);
        HashMap<Integer, FolderAccess> users = getAccessForFolderId(folderId);

        for (int i = 0; i < posts.size(); i++) {
            Post _post = posts.get(i);

            try (Response response = endpointFoldersPostsDelete(folderId, _post.getId())) {
                if (response.body() == null) { throw new MalformedResponseException(); }
            } catch (IOException e) { throw new IncompleteRequestException(); }
        }

        for (Map.Entry<Integer, FolderAccess> set : users.entrySet()) {
            try (Response response = endpointFoldersUsersDelete(folderId, set.getKey())) {
                if (response.body() == null) { throw new MalformedResponseException(); }
            } catch (IOException e) { throw new IncompleteRequestException(); }
        }

        try (Response response = endpointFoldersDelete(folderId)) {
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
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/users/login/%s/%s", email, password))))
                .build());
    }

    private static Response endpointUsersAdd(String email, String username, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("email=%s&username=%s&password=%s", email, username, password));
        return performHttpRequest(new Request.Builder()
                .url(Objects.requireNonNull(URL("/users/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build());
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

