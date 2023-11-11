package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.poste.R;

import static com.example.poste.utils.ValidationUtils.validateEmail;
import static com.example.poste.utils.ValidationUtils.validateNames;
import static com.example.poste.utils.ValidationUtils.validatePassword;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RegisterRequest;
import com.example.poste.http.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;

    private EditText firstNameText;

    private EditText lastNameText;

    private EditText passwordEditText;

    private EditText confirmPasswordEditText;

    private Button registerButton;
    private Button backButton;

    @Override
    public void onBackPressed() {
        Intent back = new Intent(RegisterActivity.this, IntroActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.editTextEmail);
        firstNameText = findViewById(R.id.editTextFirstName);
        lastNameText = findViewById(R.id.editTextLastName);
        passwordEditText = findViewById(R.id.editTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        registerButton = findViewById(R.id.buttonRegister);
        backButton = findViewById(R.id.buttonBack);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the values
                String email = emailEditText.getText().toString();
                String firstName = firstNameText.getText().toString().trim();
                String lastName = lastNameText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                if (!validateEmail(RegisterActivity.this, email)) {
                    return;
                }
                if (!validateNames(RegisterActivity.this, firstName, lastName)) {
                    return;
                }
                if (!validatePassword(RegisterActivity.this, password, confirmPassword)){
                    return;
                }
                MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
                Call<ResponseBody> call = apiService.registerUser(new RegisterRequest(email, firstName, lastName, password));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            // open dashboard activity
                            Intent dashboardIntent = new Intent(RegisterActivity.this, IntroActivity.class);
                            startActivity(dashboardIntent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        backButton.setOnClickListener(v -> {
            // Simulate a back press; go back to LandingActivity
            getOnBackPressedDispatcher().onBackPressed();
            finish();
        });
    }
}