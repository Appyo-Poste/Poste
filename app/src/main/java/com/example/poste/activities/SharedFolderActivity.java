package com.example.poste.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import com.example.poste.R;
import com.example.poste.models.FolderAccess;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.http.UpdateFolderPermissionsRequest;
import com.example.poste.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The AccountActivity class adds functionality to the activity_shared_folder.xml layout
 */
public class SharedFolderActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_shared_folder);

        TextView folderNameView = findViewById(R.id.textViewShareFolderName);
        EditText emailView = findViewById(R.id.editTextEmailToShareWith);

        Spinner permissionsSpinner = findViewById(R.id.share_folder_permissions_spinner);
        ArrayAdapter<CharSequence> permissionsSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_values, android.R.layout.simple_spinner_item);
        permissionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        permissionsSpinner.setAdapter(permissionsSpinnerAdapter);

        Intent intent = getIntent();
        folderNameView.setText(intent.getStringExtra("folderName"));

        Button saveBtn = findViewById(R.id.buttonSaveChanges);
        Button cancelBtn = findViewById(R.id.buttonCancelChanges);

        saveBtn.setOnClickListener(view -> {
            String permissionsSelectedAccessValue = permissionsSpinner.getSelectedItem().toString();
            try {
                String folderId = intent.getStringExtra("folderId");
                String email = emailView.getText().toString();
                FolderAccess selectedAccess = null;
                switch (permissionsSelectedAccessValue) {
                    case "View Access": selectedAccess = FolderAccess.VIEWER; break;
                    case "Edit Access": selectedAccess = FolderAccess.EDITOR; break;
                    case "Full Access": selectedAccess = FolderAccess.FULL_ACCESS; break;
                    default: selectedAccess = FolderAccess.NONE; break;
                }

                MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
                Call<ResponseBody> call = apiService.updateFolderPermissions(
                        User.getUser().getTokenHeaderString(),
                        new UpdateFolderPermissionsRequest(folderId, email, selectedAccess));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(SharedFolderActivity.this,
                                    getString(R.string.share_folder_success),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SharedFolderActivity.this,
                                    getString(R.string.error_message) + response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(SharedFolderActivity.this,
                                getString(R.string.Edit_shared_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.internal_error, Toast.LENGTH_LONG).show();
            }
            finish();
        });

        cancelBtn.setOnClickListener(view -> {
            finish();
        });
    }
}