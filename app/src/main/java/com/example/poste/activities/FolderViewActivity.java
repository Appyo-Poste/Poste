package com.example.poste.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.adapters.PostAdapter;
import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.Post;

import java.net.URL;
import java.util.Objects;

public class FolderViewActivity extends AppCompatActivity {

    private Folder currentFolder;
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_folder_view);

        // Prep vars
        TextView emptyText = findViewById(R.id.folderViewEmptyText);
        postRecyclerView = findViewById(R.id.posts_recycler_view);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(FolderViewActivity.this));
        registerForContextMenu(postRecyclerView);

        // Get current folder
        int currentFolderId = getIntent().getIntExtra("folderId", -1);
        try {
            currentFolder = API.getFolderById(currentFolderId);

            // Remove empty text if posts exist in folder
            if (currentFolder.getPosts().size() > 0) {
                emptyText.setVisibility(View.GONE);
            }

            // Set PostAdapter as the adapter for RecyclerView.
            postAdapter = new PostAdapter(
                    new PostAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, Post post) {
//                            Toast.makeText(FolderViewActivity.this, "Share this folder", Toast.LENGTH_LONG).show();
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLink()));
                                startActivity(browserIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(FolderViewActivity.this, R.string.internal_error, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onItemLongClick(int position, Post model) {
//                            Toast.makeText(FolderViewActivity.this, "Share this folder", Toast.LENGTH_LONG).show();
                        }
                    },
                    currentFolder.getPosts()
            );
            postRecyclerView.setAdapter(postAdapter);
        } catch (APIException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Post post = postAdapter.getLocalDataSetItem();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_post:
                intent = new Intent(FolderViewActivity.this, EditPostActivity.class);
                intent.putExtra("folderId", currentFolder.getId());
                intent.putExtra("postId", post.getId());
                startActivity(intent);
                break;
            case R.id.ctx_menu_delete_post:
                try {
                    // Remove post from folder
                    API.removePostFromFolder(post.getId(), currentFolder.getId());

                    // Delete post
                    API.deletePost(post.getId());
                } catch (APIException e) {
                    e.printStackTrace();
                    Toast.makeText(FolderViewActivity.this, R.string.internal_error, Toast.LENGTH_LONG).show();
                }

                Intent newIntent = new Intent(FolderViewActivity.this, DashboardActivity.class);
                startActivity(newIntent);
                break;
        }
        return true;
    }

}