package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.poste.R;
import com.example.poste.models.Folder;
import com.example.poste.models.Post;
import com.example.poste.models.User;
import com.example.poste.utils.DebugUtils;

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
                currentPost.setTitle(postTitle.getText().toString());
                currentPost.setDescription(postDescription.getText().toString());
                currentPost.setUrl(linkToPost.getText().toString());
            }
        });

    }
}