package com.example.poste.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
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
        postRecyclerView = findViewById(R.id.posts_recycler_view);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(FolderViewActivity.this));
        registerForContextMenu(postRecyclerView);

        // Get current folder
        int currentFolderId = getIntent().getIntExtra("folderId", -1);
        try {
            API.getFolderById(currentFolderId);
        } catch (APIException e) {
            throw new RuntimeException(e);
        }


        // new AsyncGetTweets().execute(folderId);
    }

    private class AsyncGetTweets extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(FolderViewActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                postAdapter = new PostAdapter(
                    new PostAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, Post model) {
                            Toast.makeText(FolderViewActivity.this, "Share this folder", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onItemLongClick(int position, Post model) {
                            Toast.makeText(FolderViewActivity.this, "Share this folder", Toast.LENGTH_LONG).show();

                        }
                    },
                    currentFolder.getPosts()
                );
                // Set PostAdapter as the adapter for RecyclerView.
                runOnUiThread(() -> postRecyclerView.setAdapter(postAdapter));
                return "Success";
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

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
        }
        return true;
    }

}