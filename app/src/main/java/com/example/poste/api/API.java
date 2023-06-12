package com.example.poste.api;

import com.example.poste.api.exceptions.EmailAlreadyUsedException;
import com.example.poste.api.exceptions.IncompleteRequestException;
import com.example.poste.api.exceptions.MalformedResponseException;
import com.example.poste.api.exceptions.NoUserFoundException;
import com.example.poste.api.models.Post;
import com.example.poste.api.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {

    private static final String HOST_URL = "https://poste-388415.uc.r.appspot.com/";

    public static URL URL(String endpoint) {
        try {
            return new URL(String.join("", HOST_URL, endpoint));
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
            return null;
        }
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

    public static boolean addPost(String name, String link, int ownerId) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointPostsAdd(name, link, ownerId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
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




    private static Response endpointUsers() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/users")))
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointUsersId(String id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/users/id/%s", id))))
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointUsersEmail(String email) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/users/email/%s", email))))
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointUsersLogin(String email, String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/users/login/%s/%s", email, password))))
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointUsersAdd(String email, String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("email=%s&username=%s&password=%s", email, username, password));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/users/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointUsersUpdate(String email, String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("email=%s&username=%s&password=%s", email, username, password));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/users/update")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointUsersDelete(String email, String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("email=%s&password=%s", email, password));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/users/delete")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointPosts() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts")))
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointPostsId(int id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/posts/id/%s", id))))
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointPostsUser(int id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL(String.format("/posts/user/%s", id))))
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointPostsAdd(String name, String link, int ownerId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("name=%s&link=%s&ownerId=%d", name, link, ownerId));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointPostsUpdate(int id, String name, String link, int ownerId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("id=%s&name=%s&link=%s&ownerId=%d", id, name, link, ownerId));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts/update")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }

    private static Response endpointPostsDelete(int id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("id=%d", id));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/posts/delete")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }
}

/*
    Folder place WIP NOT DONE MK (will format and sort when done)
     */

    public static boolean addFolder(String name, int ownerId) throws MalformedResponseException, IncompleteRequestException {
        try (Response response = endpointFoldersAdd(name, ownerId)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONObject responseJson = new JSONObject(response.body().string()).getJSONObject("result");

            return responseJson.getBoolean("success");
        } catch (JSONException e) { e.printStackTrace(); throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }
    }

    private static Response endpointFoldersAdd(String name, int ownerId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("name=%s&ownerId=%d", name, ownerId));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(URL("/folders/add")))
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }

    public static ArrayList<Post> getFoldersForOwnerId(int _id) throws MalformedResponseException, IncompleteRequestException {
        ArrayList<Folder> folders = new ArrayList<>();

        try (Response response = endpointPostsFolder(_id)) {
            if (response.body() == null) { throw new MalformedResponseException(); }
            JSONArray responseJson = new JSONObject(response.body().string()).getJSONArray("result");

            for (int i = 0; i < responseJson.length(); i++)
            {
                try {

                    //TODO

                    folders.add(new Folder(name, ownerId));
                } catch (JSONException e) { throw new MalformedResponseException(); }
            }
        } catch (JSONException e) { throw new MalformedResponseException(); }
        catch (IOException e) { throw new IncompleteRequestException(); }

        return folders;
    }

