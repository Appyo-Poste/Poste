package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.adapters.FolderAdapter;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.User;
import com.example.poste.api.poste.exceptions.IncompleteRequestException;
import com.example.poste.api.poste.exceptions.MalformedResponseException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        // Prep vars
        Post currentPost;
        Folder currentFolder;
        HashMap<Folder, FolderAccess> userFolders;
        HashMap<String, Integer> folderIdNameMap = new HashMap<>();
        EditText postNameField = findViewById(R.id.editTextNewName);
        EditText postLinkField = findViewById(R.id.editTextNewLink);
        Spinner spinner = findViewById(R.id.edit_post_spinner);
        Button cancelBtn = findViewById(R.id.edit_post_cancel_btn);
        Button saveBtn = findViewById(R.id.edit_post_save_button);
        try {
            currentPost = API.getPostById(Integer.parseInt(getIntent().getStringExtra("postId")));
            currentFolder = API.getFolderById(Integer.parseInt(getIntent().getStringExtra("folderId")));
            userFolders = API.getFoldersForUserId(PosteApplication.getCurrentUser().getId());
            for (Folder folder: userFolders.keySet()) {
                folderIdNameMap.put(String.format("(%d) %s", folder.getId(), folder.getName()), folder.getId());
            }
        } catch (APIException e) {
            throw new RuntimeException(e);
        }

        // Setup for the dropdown menu
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<>(folderIdNameMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Cancel button click handler
        cancelBtn.setOnClickListener(view -> {
            // Send back to folder view
            goToFolderView(currentFolder.getId());
        });

        // Save button click handler
        saveBtn.setOnClickListener(view -> {
            try {
                String selectedFolderName = spinner.getSelectedItem().toString();
                Integer selectedFolderId = folderIdNameMap.get(selectedFolderName);

                // Save the post in the API
                if (currentPost.getName() != postNameField.getText().toString() ||
                        currentPost.getLink() != postLinkField.getText().toString()) {
                    API.updatePost(currentPost.getId(), postLinkField.getText().toString(), postNameField.getText().toString(), currentPost.getOwnerId());
                }

                // Update the post in the API
                if (currentFolder.getId() != selectedFolderId) {
                    API.removePostFromFolder(currentPost.getId(), currentFolder.getId());
                    API.addPostToFolder(currentPost.getId(), selectedFolderId);
                }

                // Send back to folder view
                goToFolderView(currentFolder.getId());
            } catch (APIException e) {
                throw new RuntimeException(e);
            }
        });

        // Add folder button
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
                Toast.makeText(EditPostActivity.this, "Post created:\nName: " + itemName + "\nLink: " + itemLink, Toast.LENGTH_LONG).show();

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

    private void goToFolderView(int folderId) {
        Intent intent = new Intent(EditPostActivity.this, FolderViewActivity.class);
        intent.putExtra("folderId", folderId);
        startActivity(intent);
    }
}