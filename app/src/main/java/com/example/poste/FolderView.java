package com.example.poste;

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

import com.example.PostAdapter;
import com.example.poste.database.entity.Folder;
import com.example.poste.database.entity.PosteItem;

public class FolderView extends AppCompatActivity {

    private PostAdapter mPostAdapter;
    private RecyclerView mPostRecyclerView;
    private String folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_folder_view);
        mPostRecyclerView = findViewById(R.id.posts_recycler_view);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(FolderView.this));
        registerForContextMenu(mPostRecyclerView);

        folderId = getIntent().getStringExtra("folderId");
        new AsyncGetTweets().execute(folderId);
    }

    private class AsyncGetTweets extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(FolderView.this);
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
                mPostAdapter = new PostAdapter(params[0], new PostAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, PosteItem model) {
                        Toast.makeText(FolderView.this, "Share this folder", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onItemLongClick(int position, PosteItem model) {
                        Toast.makeText(FolderView.this, "Share this folder", Toast.LENGTH_LONG).show();

                    }
                });
                // Set PostAdapter as the adapter for RecyclerView.
                runOnUiThread(() -> mPostRecyclerView.setAdapter(mPostAdapter));
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
        PosteItem post = mPostAdapter.getLocalDataSetItem();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_post:
                intent = new Intent(FolderView.this, EditPost.class);
                intent.putExtra("folderId", folderId);
                intent.putExtra("postId", post.poste_item_id);
                startActivity(intent);
                break;
        }
        return true;
    }

}