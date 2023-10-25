package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.poste.R;
import com.example.poste.http.EditPostRequest;
import com.example.poste.http.LoginRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.models.Folder;
import com.example.poste.models.Post;
import com.example.poste.models.User;
import com.example.poste.utils.DebugUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The EditPostActivity class adds functionality to the activity_edit_post.xml layout
 */
public class EditPostActivity extends AppCompatActivity {

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        DebugUtils.logUserFoldersAndPosts(User.getUser());

        EditText linkToPost = findViewById(R.id.editTextLinkToPost);
        EditText postTitle = findViewById(R.id.editTextPostTitle);
        EditText postDescription = findViewById(R.id.editTextPostDescription);

        Button buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        Button buttonCancelChanges = findViewById(R.id.buttonCancelChanges);

        String postId = getIntent().getStringExtra("postID");
        String folderId = getIntent().getStringExtra("folderID");

        Post currentPost;
        Folder currentFolder = User.getUser().getFolder(folderId);
        if (currentFolder == null){
            currentPost = null;
            Log.e("Error", "onCreate: Folder not found");
        } else {
            currentPost = User.getUser().getFolder(folderId).getPostFromFolder(postId);
            if (currentPost == null){
                Log.e("Error", "onCreate: Post not found");
            } else {
                postTitle.setText(currentPost.getTitle());
                postDescription.setText(currentPost.getDescription());
                linkToPost.setText(currentPost.getUrl());
            }
        }

        buttonCancelChanges.setOnClickListener(view -> {
            Intent returnToFolderIntent = new Intent(EditPostActivity.this, EditPostActivity.class);
            finish();
            returnToFolderIntent.putExtra("postID",postId);
            returnToFolderIntent.putExtra("folderID",folderId);
            startActivity(returnToFolderIntent);
        });
        buttonSaveChanges.setOnClickListener(view -> {
            if (currentPost!= null){
                String title = postTitle.getText().toString();
                String description = postDescription.getText().toString();
                String url = linkToPost.getText().toString();

                MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
                Call<ResponseBody> call = apiService.editPost(User.getUser().getTokenHeader(),
                        postId,
                        new EditPostRequest(title,description,url));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            currentPost.setTitle(title);
                            currentPost.setDescription(description);
                            currentPost.setUrl(url);
                            Toast.makeText(EditPostActivity.this,"Edit post successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditPostActivity.this,"Post to edit not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditPostActivity.this, "Edit failed due to server error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}