package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.poste.api.poste.models.User;

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

        Spinner spinner = findViewById(R.id.spinner);
        String selectedValue = spinner.getSelectedItem().toString();

        Intent intent = getIntent();
        folderNameView.setText(intent.getStringExtra("folderName"));
        int folderId = intent.getIntExtra("folderId", -1);
        String folderName = intent.getStringExtra("folderName");

        Button saveBtn = findViewById(R.id.share_folder_save_btn);
        Button cancelBtn = findViewById(R.id.share_folder_cancel_btn);

        saveBtn.setOnClickListener(view -> {
            try {
                API.getFolderById(folderId);
            } catch (APIException e) {
                throw new RuntimeException(e);
            }


//            Folder folder = new Folder();
//            folder.folder_id = folderId;
//            folder.folder_name = folderName;
//            folder.shared = true;
            Intent newIntent = new Intent(Shared_Folder.this, DashboardActivity.class);
//            AppRepository appRepository = new AppRepository(PosteApplication.getApp());
//            appRepository.updateFolder(folder);
//            UserFolder userFolder = new UserFolder();
            String username = emailView.getText().toString();
//            userFolder.email = username;
//            userFolder.folder_id = String.valueOf(folderId);
            try {
                User user = API.getUserByEmail(username);
                if(user != null) {
//                    appRepository.insertUserFolder(userFolder);
//                    appRepository.updateFolder(folder);
                    startActivity(newIntent);
                    finish();
                }else{
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_LONG).show();
                }
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_LONG).show();
            }
        });

        cancelBtn.setOnClickListener(view -> {
            finish();
        });
    }
}