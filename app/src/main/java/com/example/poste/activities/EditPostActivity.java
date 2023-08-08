package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
        String currentFolderTitle;
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
            currentPost = API.getPostById(getIntent().getIntExtra("postId", 0));
            currentFolder = API.getFolderById(getIntent().getIntExtra("folderId", 0));
            currentFolderTitle = String.format("(%d) %s", currentFolder.getId(), currentFolder.getName());
            userFolders = API.getFoldersForUserId(PosteApplication.getCurrentUser().getId());
            for (Folder folder: userFolders.keySet()) {
                folderIdNameMap.put(String.format("(%d) %s", folder.getId(), folder.getName()), folder.getId());
            }
        } catch (APIException e) {
            throw new RuntimeException(e);
        }

        // Setup for the dropdown menu
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<>(folderIdNameMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set the default values
        postNameField.setText(currentPost.getName());
        postLinkField.setText(currentPost.getLink());
        spinner.setSelection(adapter.getPosition(currentFolderTitle));

        // Cancel button click handler
        cancelBtn.setOnClickListener(view -> {
            // Send back to folder view
            sendToDashboard();
        });

        // Save button click handler
        saveBtn.setOnClickListener(view -> {
            try {
                String selectedFolderName = spinner.getSelectedItem().toString();
                Integer selectedFolderId = folderIdNameMap.get(selectedFolderName);

                // Save the post in the API
                if (currentPost.getName() != postNameField.getText().toString() ||
                        currentPost.getLink() != postLinkField.getText().toString()) {
                    API.updatePost(currentPost.getId(), postNameField.getText().toString(), postLinkField.getText().toString(), currentPost.getOwnerId());
                }

                // Update the post in the API
                if (currentFolder.getId() != selectedFolderId) {
                    API.removePostFromFolder(currentPost.getId(), currentFolder.getId());
                    API.addPostToFolder(currentPost.getId(), selectedFolderId);
                }

                // Send back to folder view
                sendToDashboard();
            } catch (APIException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void sendToDashboard() {
        Intent newIntent = new Intent(EditPostActivity.this, DashboardActivity.class);
        startActivity(newIntent);
    }
}