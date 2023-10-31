package com.example.poste.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.adapters.FolderAdapter;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.User;

import java.util.HashMap;
import java.util.Objects;

/**
 * The EditFolderActivity class adds functionality to the activity_edit_folder.xml layout
 */
@SuppressLint("UseSwitchCompatOrMaterialCode")
public class EditFolderActivity extends AppCompatActivity {

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
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();

        // Set the activity layout
        setContentView(R.layout.activity_edit_folder);

        // Prep vars
        Intent intent = getIntent();
        Button cancelBtn = findViewById(R.id.edit_folder_cancel_btn);
        Button saveBtn = findViewById(R.id.edit_folder_save_button);
        EditText folderNameView = findViewById(R.id.edit_folder_folder_name);

        // Set text and checked
        folderNameView.setText(intent.getStringExtra("folderName"));

        // Save button push
        saveBtn.setOnClickListener(view -> {
            try {
                // Find folder
                int targetFolderId = intent.getIntExtra("folderId",-1);
                Folder targetFolder = API.getFolderById(targetFolderId);

                // Apply changes
                targetFolder.setName(folderNameView.getText().toString());

                // Commit changes
                targetFolder.update();
            } catch (APIException e) {
                throw new RuntimeException(e);
            }

            sendToDashboard();
        });

        // Cancel button push
        cancelBtn.setOnClickListener(view -> {
            sendToDashboard();
        });

    }

    private void sendToDashboard() {
        Intent newIntent = new Intent(EditFolderActivity.this, DashboardActivity.class);
        startActivity(newIntent);
    }
}