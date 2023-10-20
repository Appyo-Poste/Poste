package com.example.poste.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.PosteApplication;
import com.example.poste.adapters.FolderAdapter;
import com.example.poste.adapters.PostAdapter;
import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.Post;
import com.example.poste.api.poste.models.User;

import java.util.HashMap;
import java.net.URL;
import java.util.Objects;

/**
 * The FolderViewActivity class adds functionality to the activity_folder_view.xml layout
 */
public class FolderViewActivity extends AppCompatActivity {

    private Folder currentFolder;
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;
    private User currentUser;
    private HashMap<Folder, FolderAccess> userFolders;
    private FolderAdapter folderAdapter;
    public ImageView optionsView;

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure window settings for fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Set the activity layout
        setContentView(R.layout.activity_folder_view);

        // Prep vars
        TextView emptyText = findViewById(R.id.folderViewEmptyText);
        postRecyclerView = findViewById(R.id.posts_recycler_view);
        currentUser = PosteApplication.getCurrentUser();
        currentFolder = PosteApplication.getCurrentFolder();
        registerForContextMenu(postRecyclerView);

        // Get current folder
        int currentFolderId = currentFolder.getId();

        // Remove empty text if posts exist in folder
        if (currentFolder.getPosts().size() > 0) {
            emptyText.setVisibility(View.GONE);
        }

        // Fill post view (Recycler View)
        postRecyclerView.setLayoutManager(new LinearLayoutManager(FolderViewActivity.this));
        postAdapter = new PostAdapter(
                new PostAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, Post post) {
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
        registerForContextMenu(postRecyclerView);
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
                currentFolder.removePost(post);
                Intent newIntent = new Intent(FolderViewActivity.this, DashboardActivity.class);
                startActivity(newIntent);
                break;
        }
        return true;
    }

}