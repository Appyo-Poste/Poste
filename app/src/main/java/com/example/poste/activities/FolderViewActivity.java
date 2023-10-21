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
import com.example.poste.api.poste.models.Post;

/**
 * The FolderViewActivity class adds functionality to the activity_folder_view.xml layout
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
        PosteApplication.setCurrentPost(null);

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
            Post newPost = new Post(PosteApplication.getCurrentUser().getId(), "", "", PosteApplication.getCurrentUser().getId());
            PosteApplication.getCurrentFolder().addPost(newPost);
            PosteApplication.setCurrentPost(newPost);
            Intent intent = new Intent(FolderViewActivity.this, EditPostActivity.class);
            startActivity(intent);
        });

        settingsBut.setOnClickListener(view -> {
            Intent intent = new Intent(FolderViewActivity.this, EditFolderActivity.class);
            startActivity(intent);
        });

        // Create listeners for the post buttons
        openBut.setOnClickListener(view -> {
            if (PosteApplication.getCurrentPost() != null) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PosteApplication.getCurrentPost().getLink()));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FolderViewActivity.this, R.string.internal_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        editBut.setOnClickListener(view -> {
            if (PosteApplication.getCurrentPost() != null) {
                Intent intent = new Intent(FolderViewActivity.this, EditPostActivity.class);
                startActivity(intent);
            }
        });

        deleteBut.setOnClickListener(view -> {
            if (PosteApplication.getCurrentPost() != null) {
                PosteApplication.getCurrentFolder().removePost(PosteApplication.getCurrentPost());
                Intent intent = new Intent(FolderViewActivity.this, FolderViewActivity.class);
                startActivity(intent);
            }
        });

        // Set folder name in title bar
        folderName.setText(PosteApplication.getCurrentFolder().getName());

        // Remove empty text if posts exist in folder or remove buttons if the folder is empty
        if (PosteApplication.getCurrentFolder().getPosts().size() > 0) {
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
                        PosteApplication.setCurrentPost(post);
                    }

                    @Override
                    public void onItemLongClick(int position, Post model) {
                            // No Long click action
                    }
                },
                PosteApplication.getCurrentFolder().getPosts()
        );
        postRecyclerView.setAdapter(postAdapter);
    }
}