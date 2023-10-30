package com.example.poste.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.PosteApplication;
import com.example.poste.adapters.PostAdapter;
import com.example.poste.R;
import com.example.poste.callbacks.PostDeletionCallback;
import com.example.poste.models.Post;
import com.example.poste.models.User;

/**
 * The FolderViewActivity class adds functionality to the activity_folder_view.xml layout.
 * This class governs the page where users can view, use, and edit the posts contained within a
 * selected folder.
 */
public class FolderViewActivity extends AppCompatActivity {
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear post selection
        PosteApplication.setSelectedPost(null);

        // Configure window settings for fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Set the activity layout
        setContentView(R.layout.activity_folder_view);

        // Prep vars
        TextView emptyText = findViewById(R.id.folderViewEmptyText);
        TextView folderName = findViewById(R.id.folderNameText);
        ImageButton newBut = findViewById(R.id.newPost);
        ImageButton settingsBut = findViewById(R.id.folderSettings);
        Button openBut = findViewById(R.id.buttonOpen);
        Button editBut = findViewById(R.id.buttonEdit);
        Button deleteBut = findViewById(R.id.buttonDelete);
        postRecyclerView = findViewById(R.id.posts_recycler_view);

        // Create listeners for the folder buttons
        newBut.setOnClickListener(view -> {
            PosteApplication.setSelectedPost(null);
            Intent intent = new Intent(FolderViewActivity.this, EditPostActivity.class);
            startActivity(intent);
        });

        settingsBut.setOnClickListener(view -> {
            Intent intent = new Intent(FolderViewActivity.this, EditFolderActivity.class);
            startActivity(intent);
        });

        // Create listeners for the post buttons
        openBut.setOnClickListener(view -> {
            if (PosteApplication.getSelectedPost() != null) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PosteApplication.getSelectedPost().getUrl()));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FolderViewActivity.this, R.string.internal_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        editBut.setOnClickListener(view -> {
            if (PosteApplication.getSelectedPost() != null) {
                Intent intent = new Intent(FolderViewActivity.this, EditPostActivity.class);
                startActivity(intent);
            }
        });

        deleteBut.setOnClickListener(view -> {
            if (PosteApplication.getSelectedPost() != null) {
                PostDeletionCallback postDeletionCallback = new PostDeletionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(FolderViewActivity.this, R.string.post_deleted, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FolderViewActivity.this, FolderViewActivity.class);
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(FolderViewActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                };
                User.getUser().deletePostFromServer(PosteApplication.getSelectedPost(), postDeletionCallback);
            }
        });

        // Set folder name in title bar
        folderName.setText(PosteApplication.getSelectedFolder().getTitle());

        // Remove empty text if posts exist in folder or remove buttons if the folder is empty
        if (PosteApplication.getSelectedFolder().getPosts().size() > 0) {
            emptyText.setVisibility(View.GONE);
        }
        else {
            openBut.setVisibility(View.GONE);
            editBut.setVisibility(View.GONE);
            deleteBut.setVisibility(View.GONE);
        }

        // Fill post view (Recycler View)
        postRecyclerView.setLayoutManager(new LinearLayoutManager(FolderViewActivity.this));
        postAdapter = new PostAdapter(
                new PostAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, Post post) {
                        PosteApplication.setSelectedPost(post);
                    }

                    @Override
                    public void onItemLongClick(int position, Post model) {
                            // No Long click action
                    }
                },
                PosteApplication.getSelectedFolder().getPosts()
        );
        postRecyclerView.setAdapter(postAdapter);
    }
}