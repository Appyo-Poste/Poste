package com.example.poste.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.models.Folder;
import com.example.poste.http.EditFolderRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The EditFolderActivity class adds functionality to the activity_edit_folder_v2.xml layout
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
                            Toast.makeText(EditFolderActivity_v2.this,"Edit folder successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditFolderActivity_v2.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(EditFolderActivity_v2.this, "Edit failed, unknown error", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
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
        finish();
    }
}