package com.example.poste.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
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
        postRecyclerView = findViewById(R.id.posts_recycler_view);

        // Create listeners for the folder buttons
        newBut.setOnClickListener(view -> {
            PosteApplication.setSelectedPost(null);
            Intent intent = new Intent(FolderViewActivity.this, NewPostActivity.class);
            intent.putExtra("default", PosteApplication.getSelectedFolder().getTitle());
            startActivity(intent);
        });

        settingsBut.setOnClickListener(view -> {
            Intent intent = new Intent(FolderViewActivity.this, EditFolderActivity_v2.class);
            intent.putExtra("folderId", PosteApplication.getSelectedFolder().getId());
            intent.putExtra("folderName", PosteApplication.getSelectedFolder().getTitle());
            startActivity(intent);
            finish();
        });

        // Set folder name in title bar
        folderName.setText(PosteApplication.getSelectedFolder().getTitle());

        // Remove empty text if posts exist in folder or remove buttons if the folder is empty
        if (PosteApplication.getSelectedFolder().getPosts().size() > 0) {
            emptyText.setVisibility(View.GONE);
        }

        // Fill post view (Recycler View)
        postRecyclerView.setLayoutManager(new LinearLayoutManager(FolderViewActivity.this));
        postAdapter = new PostAdapter(
                new PostAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, Post post) {
                        PosteApplication.setSelectedPost(post);
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PosteApplication.getSelectedPost().getUrl()));
                            startActivity(browserIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(FolderViewActivity.this, R.string.internal_error, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onItemLongClick(int position, Post post) {
                        PosteApplication.setSelectedPost(post);
                    }
                },
                PosteApplication.getSelectedFolder().getPosts()
        );
        postRecyclerView.setAdapter(postAdapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Post post = postAdapter.getLocalDataSetItem();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_post:
                PosteApplication.setSelectedPost(post);
                if (PosteApplication.getSelectedPost() != null) {
                    intent = new Intent(FolderViewActivity.this, EditPostActivity.class);
                    intent.putExtra("postID", post.getId());
                    intent.putExtra("folderID", PosteApplication.getSelectedFolder().getId());
                    startActivity(intent);
                }
                break;
            case R.id.ctx_menu_delete_post:
                PosteApplication.setSelectedPost(post);
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
                    finish();
                    recreate();
                }
                break;
        }
        return true;
    }
}