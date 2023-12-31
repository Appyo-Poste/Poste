package com.example.poste.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import com.example.poste.R;
import com.example.poste.http.EditFolderRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.models.Folder;
import com.example.poste.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The EditFolderActivity class adds functionality to the activity_edit_folder_v2.xml layout
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

        // Configure window settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Set the activity layout
        setContentView(R.layout.activity_edit_folder);

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
                 String folderId = intent.getStringExtra("folderId");
                 Folder currentFolder = User.getUser().getFolder(folderId);
                String title = folderNameView.getText().toString();
                MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
                Call<ResponseBody> call = apiService.editFolder(
                        User.getUser().getTokenHeaderString(),
                        folderId,
                        new EditFolderRequest(title));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            currentFolder.setTitle(title);
                            Toast.makeText(EditFolderActivity.this,
                                    getString(R.string.edit_folder_success),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditFolderActivity.this,
                                    getString(R.string.error_message) + response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditFolderActivity.this,
                                getString(R.string.edit_failure),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Cancel button push
        cancelBtn.setOnClickListener(view -> {
            finish();
        });

    }

}