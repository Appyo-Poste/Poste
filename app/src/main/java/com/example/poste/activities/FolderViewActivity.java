package com.example.poste.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.R;
import com.example.poste.adapters.PostAdapter;
import com.example.poste.callbacks.PostDeletionCallback;
import com.example.poste.callbacks.UpdateCallback;
import com.example.poste.models.Post;
import com.example.poste.models.User;

/**
 * The FolderViewActivity class adds functionality to the activity_folder_view.xml layout.
 * This class governs the page where users can view, use, and edit the posts contained within a
 * selected folder.
 */
public class FolderViewActivity extends AppCompatActivity {
    private static UpdateCallback updateCallback;
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;
    private TextView emptyText;

    private TextView folderName;

    private Button newBut;
    private ImageView settingsBut;

    @Override
    public void onBackPressed() {
        User.getUser().setSelectedPost(null);
        User.getUser().setSelectedFolder(null);
        super.onBackPressed();
    }

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User.getUser().setSelectedPost(null);
        configureWindow();
        prepVars();
        setListeners();
        updateData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    private void setListeners() {
        newBut.setOnClickListener(view -> {
            User.getUser().setSelectedPost(null);
            Intent intent = new Intent(FolderViewActivity.this, NewPostActivity.class);
            intent.putExtra("default", User.getUser().getSelectedFolder().getTitle());
            startActivity(intent);
        });

        settingsBut.setOnClickListener(view -> {
            Intent intent = new Intent(FolderViewActivity.this, EditFolderActivity.class);
            intent.putExtra("folderId", User.getUser().getSelectedFolder().getId());
            intent.putExtra("folderName", User.getUser().getSelectedFolder().getTitle());
            startActivity(intent);
        });
    }

    private void prepVars() {
        emptyText = findViewById(R.id.folderViewEmptyText);
        folderName = findViewById(R.id.folderNameText);
        newBut = findViewById(R.id.newPost);
        settingsBut = findViewById(R.id.folderSettings);
        postRecyclerView = findViewById(R.id.posts_recycler_view);
        updateCallback = new UpdateCallback() {
            @Override
            public void onSuccess() {
                // reset selected folder
                User.getUser().setSelectedFolder(User.getUser().getFolder(User.getUser().getSelectedFolder().getId()));
                // Set folder name in title bar
                folderName.setText(User.getUser().getSelectedFolder().getTitle());

                // Remove empty text if posts exist in folder or remove buttons if the folder is empty
                if (User.getUser().getSelectedFolder().getPosts().size() > 0) {
                    emptyText.setVisibility(View.GONE);
                }

                // Fill post view (Recycler View)
                postRecyclerView.setLayoutManager(new LinearLayoutManager(FolderViewActivity.this));
                postAdapter = new PostAdapter(
                        new PostAdapter.ClickListener() {
                            @Override
                            public void onItemClick(int position, Post post) {
                                User.getUser().setSelectedPost(post);
                                try {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(User.getUser().getSelectedPost().getUrl()));
                                    startActivity(browserIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(FolderViewActivity.this, R.string.internal_error, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onItemLongClick(int position, Post post) {
                                User.getUser().setSelectedPost(post);
                            }
                        },
                        User.getUser().getSelectedFolder().getPosts()
                );
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(
                        FolderViewActivity.this,
                        "Unable to retrieve folders and posts, please try again.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        };
    }

    private void configureWindow() {
        // Configure window settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Set the activity layout
        setContentView(R.layout.activity_folder_view);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Post post = postAdapter.getLocalDataSetItem();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_post:
                User.getUser().setSelectedPost(post);
                if (User.getUser().getSelectedPost() != null) {
                    intent = new Intent(FolderViewActivity.this, EditPostActivity.class);
                    intent.putExtra("postID", post.getId());
                    intent.putExtra("folderID", User.getUser().getSelectedFolder().getId());
                    startActivity(intent);
                }
                break;
            case R.id.ctx_menu_delete_post:
                User.getUser().setSelectedPost(post);
                if (User.getUser().getSelectedPost() != null) {
                    PostDeletionCallback postDeletionCallback = new PostDeletionCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(FolderViewActivity.this, R.string.post_deleted, Toast.LENGTH_LONG).show();
                            recreate();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(FolderViewActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            recreate();
                        }
                    };
                    User.getUser().deletePostFromServer(User.getUser().getSelectedPost(), postDeletionCallback);
                }
                break;
        }
        return true;
    }

    private void updateData() {
        User.getUser().updateFoldersAndPosts(updateCallback);
    }
}