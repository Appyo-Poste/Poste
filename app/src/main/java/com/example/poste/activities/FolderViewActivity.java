package com.example.poste.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import java.util.Objects;

public class FolderViewActivity extends AppCompatActivity {

    private Folder currentFolder;
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;

    private User currentUser;
    private HashMap<Folder, FolderAccess> userFolders;

    private FolderAdapter folderAdapter;
    public ImageView optionsView;

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
        currentUser = PosteApplication.getCurrentUser();
        optionsView = findViewById(R.id.Optionsbtn);
        Button addButton = findViewById(R.id.dashboard_add_folder_btn);
        registerForContextMenu(postRecyclerView);

        // Get current folder
        int currentFolderId = getIntent().getIntExtra("folderId", -1);
        try {
            currentFolder = API.getFolderById(currentFolderId);

            // Set PostAdapter as the adapter for RecyclerView.
            postAdapter = new PostAdapter(
                    new PostAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, Post model) {
//                            Toast.makeText(FolderViewActivity.this, "Share this folder", Toast.LENGTH_LONG).show();
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


        // new AsyncGetTweets().execute(folderId);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the create item dialog
                showCreateItemDialog();
            }
        });
    }

    private void showCreateItemDialog() {
        // Find the [+] button on the dashboard
        Button addButton = findViewById(R.id.dashboard_add_folder_btn);

        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, addButton);
        popupMenu.inflate(R.menu.menu_create_item);

        // Set a MenuItemClickListener to handle the selection
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_folder:
                        // Handle folder creation
                        showCreateFolderDialog();
                        return true;
                    case R.id.menu_post:
                        // Handle post creation
                        showCreatePostDialog();
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Show the PopupMenu
        popupMenu.show();
    }


    private void showCreatePostDialog() {
        // Inflate the layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_create_post, null);

        // Find the RadioGroup and EditText fields in the dialog
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        EditText editTextItemLink = dialogView.findViewById(R.id.editTextItemLink);
        Button buttonCreateItem = dialogView.findViewById(R.id.buttonCreateItem);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Create Post");

        // Set the positive button (Create button) click listener
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                // Get the item name and link from the EditText fields
                String itemName = editTextItemName.getText().toString().trim();
                String itemLink = editTextItemLink.getText().toString().trim();


                // Handle post creation logic
                // For simplicity, let's just display a toast message with the post details
                Toast.makeText(FolderViewActivity.this, "Post created:\nName: " + itemName + "\nLink: " + itemLink, Toast.LENGTH_LONG).show();

            }
        });

        // Set the negative button (Cancel button) click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog (do nothing)
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showCreateFolderDialog() {
        // Inflate the layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_create_folder, null);

        // Find the RadioGroup and EditText fields in the dialog
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        Button buttonCreateItem = dialogView.findViewById(R.id.buttonCreateItem);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Create Folder");

        // Set the positive button (Create button) click listener
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                // Get the item name and link from the EditText fields
                String itemName = editTextItemName.getText().toString().trim();

                // Handle folder creation logic
                try {
                    int newFolderId = API.addFolder(itemName, currentUser.getId());
                    API.addUserToFolder(currentUser.getId(), newFolderId, FolderAccess.OWNER);

                    userFolders = API.getFoldersForUserId(currentUser.getId());
                    folderAdapter.notifyItemInserted(userFolders.size() - 1);
                } catch (APIException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        // Set the negative button (Cancel button) click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog (do nothing)
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    private class AsyncGetTweets extends AsyncTask<String, String, String> {
//        ProgressDialog pdLoading = new ProgressDialog(FolderViewActivity.this);
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                postAdapter = new PostAdapter(
//                    new PostAdapter.ClickListener() {
//                        @Override
//                        public void onItemClick(int position, Post model) {
//                            Toast.makeText(FolderViewActivity.this, "Share this folder", Toast.LENGTH_LONG).show();
//
//                        }
//
//                        @Override
//                        public void onItemLongClick(int position, Post model) {
//                            Toast.makeText(FolderViewActivity.this, "Share this folder", Toast.LENGTH_LONG).show();
//
//                        }
//                    },
//                    currentFolder.getPosts()
//                );
//                // Set PostAdapter as the adapter for RecyclerView.
//                runOnUiThread(() -> postRecyclerView.setAdapter(postAdapter));
//                return "Success";
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return "exception";
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            //this method will be running on UI thread
//
//            pdLoading.dismiss();
//
//        }
//    }

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