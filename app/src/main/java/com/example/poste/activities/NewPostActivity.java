package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.poste.R;
import com.example.poste.callbacks.UpdateCallback;
import com.example.poste.http.CreatePostRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.models.Folder;
import com.example.poste.models.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity {

    private Intent newPostIntent;

    private Button buttonSaveChanges;

    private Button buttonCancelChanges;

    private Spinner chooseFolderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        getSupportActionBar().hide();
        newPostIntent = getIntent();
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        buttonCancelChanges = findViewById(R.id.buttonCancelChanges);
        chooseFolderSpinner = findViewById(R.id.chooseFolderSpinner);

        buttonCancelChanges.setOnClickListener(v -> {
            Log.d("NewPostActivity", "Cancel changes clicked");
            finish();
        });

        if (!User.getUser().isLoggedIn()) {
            // user needs to login
            Log.d("NewPostActivity", "User needs to login");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.putExtra("return", true);
            startActivity(loginIntent);
            // when the user logs in, LoginActivity returns and onResume will be called
        } else {
            Log.d("NewPostActivity", "User is logged in");
            Log.d("NewPostActivity", "User token: " + User.getUser().getToken());
            handleNewPost();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NewPostActivity", "onResume called; returning from LoginActivity?");
        UpdateCallback callback = new UpdateCallback() {

            @Override
            public void onSuccess() {
                Log.d("NewPostActivity", "Retrieved user data");
                handleNewPost();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("NewPostActivity", "Error retrieving user data: " + errorMessage);
                handleNewPost();
            }
        };
        User.getUser().updateFoldersAndPosts(callback);
    }


    private void handleNewPost() {
        String sharedText = null;
        if (newPostIntent != null
                && newPostIntent.getAction() != null
                && newPostIntent.getAction().equals(Intent.ACTION_SEND)
                && newPostIntent.getType() != null
                && newPostIntent.getType().equals("text/plain")
                && newPostIntent.getStringExtra(Intent.EXTRA_TEXT) != null
        ) {
            sharedText = newPostIntent.getStringExtra(Intent.EXTRA_TEXT);
        } else {
            Log.d("NewPostActivity", "No intent to pre-fill post");
        }

        if (sharedText != null) {
            EditText linkEditText = findViewById(R.id.editTextPostLink);
            linkEditText.setText(sharedText);
        } else {
            Log.d("NewPostActivity", "No shared text");
        }

        List<Folder> folders = new ArrayList<>();
        folders.add(new Folder.Builder()
                .setTitle("-- Select a Folder --")
                .setId("0")
                .build());
        // setup folder spinner
        folders.addAll(User.getUser().getFolders());
        ArrayAdapter<Folder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, folders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseFolderSpinner.setAdapter(adapter);
        // set default folder, if applicable
        String defaultFolder = newPostIntent.getStringExtra("default");
        if (defaultFolder != null) {
            for (int i = 0; i < folders.size(); i++) {
                if (folders.get(i).getTitle().equals(defaultFolder)) {
                    chooseFolderSpinner.setSelection(i);
                    break;
                }
            }
        }

        buttonSaveChanges.setOnClickListener(v ->{
            String folder_id = ((Folder) chooseFolderSpinner.getSelectedItem()).getId();
            if (folder_id.equals("0")) {
                Log.d("NewPostActivity", "Default folder (0) selected");
                Toast.makeText(this, "Select a folder to save post!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.d("NewPostActivity", "Folder id: " + folder_id);
                String title = ((EditText) findViewById(R.id.editTextPostTitle)).getText().toString();
                String url = ((EditText) findViewById(R.id.editTextPostLink)).getText().toString();
                String description = ((EditText) findViewById(R.id.editTextPostDescription)).getText().toString();


                CreatePostRequest request = new CreatePostRequest(
                        title,
                        description,
                        url,
                        folder_id
                );
                MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
                Call<ResponseBody> call = apiService.createPost(User.getUser().getTokenHeaderString(), request);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(NewPostActivity.this, "Created post!", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(NewPostActivity.this, DashboardActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(NewPostActivity.this, "Failed to create post", Toast.LENGTH_SHORT).show();
                            Log.d("NewPostActivity", "Failed to create post: " + response.code() + " " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(NewPostActivity.this, "Faied to create post", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
