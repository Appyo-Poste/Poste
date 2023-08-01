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

public class Shared_Folder extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_shared_folder);

        TextView folderNameView = findViewById(R.id.share_folder_name);
        EditText emailView = findViewById(R.id.share_folder_email);

        Spinner spinner = findViewById(R.id.share_folder_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_values, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Intent intent = getIntent();
        folderNameView.setText(intent.getStringExtra("folderName"));
        int folderId = intent.getIntExtra("folderId", -1);
        String folderName = intent.getStringExtra("folderName");

        Button saveBtn = findViewById(R.id.share_folder_save_btn);
        Button cancelBtn = findViewById(R.id.share_folder_cancel_btn);

        saveBtn.setOnClickListener(view -> {
            Intent newIntent = new Intent(Shared_Folder.this, DashboardActivity.class);
            String selectedAccessValue = spinner.getSelectedItem().toString();
            try {
                Folder targetFolder = API.getFolderById(folderId);
                User targetUser = API.getUserByEmail(emailView.getText().toString());
                FolderAccess selectedAccess = null;
                switch (selectedAccessValue) {
                    case "View Access": selectedAccess = FolderAccess.VIEW; break;
                    case "Manage Access": selectedAccess = FolderAccess.MANAGE; break;
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
            finish();
        });
    }
}