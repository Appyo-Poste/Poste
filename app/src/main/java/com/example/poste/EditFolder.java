package com.example.poste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.PosteApplication;
import com.example.poste.database.AppRepository;
import com.example.poste.database.entity.Folder;

public class EditFolder extends AppCompatActivity {

    private EditText folderNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);
        getSupportActionBar().hide();
        folderNameView = findViewById(R.id.edit_folder_folder_name);
        Switch folderSharedSwitch = findViewById(R.id.edit_folder_shared_switch);

        Intent intent = getIntent();
        folderNameView.setText(intent.getStringExtra("folderName"));
        folderSharedSwitch.setChecked(intent.getBooleanExtra("folderShared", false));

        Button saveBtn = findViewById(R.id.edit_folder_save_button);

        saveBtn.setOnClickListener(view -> {
            Folder folder = new Folder();
            folder.folder_id = intent.getIntExtra("folderId",-1);
            folder.folder_name = folderNameView.getText().toString();
            folder.shared = folderSharedSwitch.isChecked();
            Intent newIntent = new Intent(EditFolder.this, Dashboard.class);
            AppRepository appRepository = new AppRepository(PosteApplication.getApp());
            appRepository.updateFolder(folder);
            startActivity(newIntent);
        });

    }
}