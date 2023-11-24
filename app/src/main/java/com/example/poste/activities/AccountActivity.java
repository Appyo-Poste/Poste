package com.example.poste.activities;

import static com.example.poste.utils.ValidationUtils.validatePassword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.poste.R;
import com.example.poste.http.EditAccountRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The AccountActivity class adds functionality to the activity_account.xml layout
 */
public class AccountActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_account);

        // Prep vars
        Button buttonSave = findViewById(R.id.AccountSavebtn);

        EditText oldPasswordEditText = findViewById(R.id.AccountCurrentPasswordEditText);
        EditText newPasswordEditText = findViewById(R.id.AccountNewPasswordEnter);
        EditText newPasswordConfirmEditText = findViewById(R.id.AccountConfirmPasswordEnter);
        // Click listener for the save account button
        buttonSave.setOnClickListener(view -> {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String newPasswordConfirm = newPasswordConfirmEditText.getText().toString();

            if (!validatePassword(AccountActivity.this, newPassword, newPasswordConfirm)){
                return;
            }
            MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
            Call<ResponseBody> call = apiService.editUser(
                        User.getUser().getTokenHeaderString(),
                        new EditAccountRequest(oldPassword, newPassword)
                                    );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(AccountActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        String jsonResponse;
                        try {
                            jsonResponse = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        JSONObject jsonObject = null;
                        String token;
                        try {
                            jsonObject = new JSONObject(jsonResponse);
                            token = jsonObject.getJSONObject("result").getString("token");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("debug","Token: " + token);
                        User.getUser().setToken(token);

                        // Send to dashboard page
                        Intent intent = new Intent(AccountActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        String errorMessage;
                        try {
                            errorMessage = new JSONObject(response.errorBody().string()).getString("error");
                            Log.e("Error", errorMessage );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if (errorMessage != null) {
                            Toast.makeText(AccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountActivity.this, "Password change failed, unknown error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AccountActivity.this, "Edit failed, unknown error", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}