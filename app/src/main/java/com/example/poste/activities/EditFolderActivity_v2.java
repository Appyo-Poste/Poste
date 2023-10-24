package com.example.poste.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.Folder;

/**
 * The EditFolderActivity class adds functionality to the activity_edit_folder.xml layout
 */
@SuppressLint("UseSwitchCompatOrMaterialCode")
public class EditFolderActivity_v2 extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_folder_v2);

        // Prep vars
        Intent intent = getIntent();
        Button cancelBtn = findViewById(R.id.buttonCancelChanges);
        Button saveBtn = findViewById(R.id.buttonSaveChanges);
        EditText folderNameView = findViewById(R.id.editTextNameOfFolder);

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
        Intent newIntent = new Intent(EditFolderActivity_v2.this, DashboardActivity.class);
        startActivity(newIntent);
    }
}