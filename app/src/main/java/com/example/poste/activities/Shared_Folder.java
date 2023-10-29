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
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.models.User;

import java.util.HashMap;

/**
 * The AccountActivity class adds functionality to the activity_shared_folder.xml layout
 */
public class Shared_Folder extends AppCompatActivity {
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

        cancelBtn.setOnClickListener(view -> {
            Intent newIntent = new Intent(Shared_Folder.this, DashboardActivity.class);
            startActivity(newIntent);
        });
    }
}