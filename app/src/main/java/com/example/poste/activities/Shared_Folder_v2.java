package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.exceptions.IncompleteRequestException;
import com.example.poste.api.poste.exceptions.MalformedResponseException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.User;

import java.util.HashMap;

/**
 * The AccountActivity class adds functionality to the activity_shared_folder_v2.xml layout
 */
public class Shared_Folder_v2 extends AppCompatActivity {
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
        setContentView(R.layout.activity_shared_folder_v2);

        TextView folderNameView = findViewById(R.id.textViewShareFolderName);
        EditText emailView = findViewById(R.id.editTextEmailToShareWith);
        TextView folderPermissionsView = findViewById(R.id.textViewShareFolderPermissions);

        Spinner folderSpinner = findViewById(R.id.share_folder_spinner);
        Spinner permissionsSpinner = findViewById(R.id.share_folder_permissions_spinner);
        ArrayAdapter<CharSequence> folderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_values, android.R.layout.simple_spinner_item);
        folderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        folderSpinner.setAdapter(folderSpinnerAdapter);
        ArrayAdapter<CharSequence> permissionsSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_values, android.R.layout.simple_spinner_item);
        permissionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        permissionsSpinner.setAdapter(permissionsSpinnerAdapter);

        Intent intent = getIntent();
        folderNameView.setText(intent.getStringExtra("folderName"));
        int folderId = intent.getIntExtra("folderId", -1);
        String folderName = intent.getStringExtra("folderName");

        Button saveBtn = findViewById(R.id.buttonSaveChanges);
        Button cancelBtn = findViewById(R.id.buttonCancelChanges);

        saveBtn.setOnClickListener(view -> {
            Intent newIntent = new Intent(Shared_Folder_v2.this, DashboardActivity.class);
            String folderSelectedAccessValue = folderSpinner.getSelectedItem().toString();
            String permissionsSelectedAccessValue = permissionsSpinner.getSelectedItem().toString();
            try {
                Folder targetFolder = API.getFolderById(folderId);
                User targetUser = API.getUserByEmail(emailView.getText().toString());
                FolderAccess selectedAccess = null;
                switch (permissionsSelectedAccessValue) {
                    case "No Access": selectedAccess = FolderAccess.NONE; break;
                    case "View Access": selectedAccess = FolderAccess.VIEWER; break;
                    case "Edit Access": selectedAccess = FolderAccess.EDITOR; break;
                    case "Full Access": selectedAccess = FolderAccess.FULL_ACCESS; break;
                    default: selectedAccess = FolderAccess.NONE; break;
                }

                HashMap<Folder, FolderAccess> targetUserFolders = API.getFoldersForUserId(targetUser.getId());

                boolean result = false;
                if (targetUserFolders.keySet().contains(targetFolder)) {
                    result = API.updateUserAccessToFolder(targetUser.getId(), targetFolder.getId(), selectedAccess);
                } else {
                    result = API.addUserToFolder(targetUser.getId(), targetFolder.getId(), selectedAccess);
                }

                if (result) {
                    Toast.makeText(this, R.string.shared_folder_share_success, Toast.LENGTH_LONG).show();
                    startActivity(newIntent);
                    finish();
                }

            } catch (APIException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.internal_error, Toast.LENGTH_LONG).show();
            }
        });

        cancelBtn.setOnClickListener(view -> {
            Intent newIntent = new Intent(Shared_Folder_v2.this, DashboardActivity.class);
            startActivity(newIntent);
        });
    }
}