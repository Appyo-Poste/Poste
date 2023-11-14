package com.example.poste.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;
import com.example.poste.http.EditPostRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.models.Post;
import com.example.poste.models.User;
import com.example.poste.utils.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The EditPostActivity class adds functionality to the activity_edit_post.xml layout
 *
 * The activity that calls this activity, do a .putExtra to pass the postId
 *
 * Example:
 *  Intent editPostIntent = new Intent(FolderViewActivity.this, EditPostActivity.class);
 *  finish();
 *  editPostIntent.putExtra("postID",postId);
 *  editPostIntent.putExtra("folderID",folderId);
 *  startActivity(editPostIntent);
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
        getSupportActionBar().hide();
//        DebugUtils.logUserFoldersAndPosts(User.getUser());

        EditText postLink = findViewById(R.id.editTextPostLink);
        EditText postTitle = findViewById(R.id.editTextPostTitle);
        EditText postDescription = findViewById(R.id.editTextPostDescription);

        Button buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        Button buttonCancelChanges = findViewById(R.id.buttonCancelChanges);

        Post currentPost;

        String postId = getIntent().getStringExtra("postID");

        if (postId == null){
            currentPost = null;
            Log.e("Error", "EditPostActivity onCreate: Post not found");
        } else {
            currentPost = User.getUser().getPost(postId);
        }

        if (currentPost == null){
            Log.e("Error", "EditPostActivity onCreate: Post not found");
        } else {
            postTitle.setText(currentPost.getTitle());
            postDescription.setText(currentPost.getDescription());
            postLink.setText(currentPost.getUrl());
        }

        buttonCancelChanges.setOnClickListener(view -> {
            finish();
        });

        buttonSaveChanges.setOnClickListener(view -> {
            if (currentPost != null){
                String title = postTitle.getText().toString();
                String description = postDescription.getText().toString();
                String url = postLink.getText().toString();

                MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
                Call<ResponseBody> call = apiService.editPost(
                        User.getUser().getTokenHeaderString(),
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
                            finish();
                        } else {
                            String errorMessage = utils.parseError(response);
                            if (errorMessage != null) {
                                Toast.makeText(EditPostActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditPostActivity.this, "Edit failed, unknown error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditPostActivity.this, "Edit failed, unknown error", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}