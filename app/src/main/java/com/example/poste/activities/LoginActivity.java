package com.example.poste.activities;

import static com.example.poste.utils.ValidationUtils.validateEmail;
import static com.example.poste.utils.ValidationUtils.validatePassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poste.R;
import com.example.poste.http.LoginRequest;
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

public class LoginActivity extends AppCompatActivity {

    public Button buttonLoginSubmit;
    private EditText usernameField, passwordField;

    @Override
    public void onBackPressed() {
        Intent back = new Intent(LoginActivity.this, IntroActivity.class);
        startActivity(back);
        finish();
    }

    /**
     * function that runs on creation of the activity
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView hyperlinkTextView = findViewById(R.id.hyperlinkTextViewToRegister);
        usernameField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);

        buttonLoginSubmit = findViewById(R.id.loginLoginbtn);
        Boolean isReturn = getIntent().getBooleanExtra("return", false); // if true, dont start dashboard activity, just finish this one
        Log.d("debug", "isReturn: " + isReturn);

        // adds a hyperlink to redirect user to the login page
        SpannableString spannable = new SpannableString(hyperlinkTextView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Start the SecondActivity when the link is clicked
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        };

        spannable.setSpan(clickableSpan, 0, hyperlinkTextView.getText().length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        hyperlinkTextView.setText(spannable);
        hyperlinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        /**
         * Function that runs when login button is pressed
         * Makes a request to the API and gets a response in return
         */
        buttonLoginSubmit.setOnClickListener(view -> {
            String email = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            if (!validateEmail(LoginActivity.this, email)){
                return;
            }
            if (!validatePassword(LoginActivity.this, password)){
                return;
            }
            MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
            Call<ResponseBody> call = apiService.loginUser(new LoginRequest(email, password));
            call.enqueue(new Callback<ResponseBody>() {
                /**
                 * Function that runs when response is returned by the API
                 * @param call
                 * @param response Response returned by the API
                 */
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Login successful!", Toast.LENGTH_SHORT).show();
                        try {
                            String jsonResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String token = jsonObject.getJSONObject("result").getString("token");
                            Log.d("debug","Token: " + token);
                            User.getUser().setToken(token);
                            User.getUser().setEmail(email);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException err){
                            Log.e("Json Error", err.toString());
                        }
                        if (isReturn){
                            Log.d("debug", "isReturn is true");
                            Log.d("debug", "Finishing activity, returning to previous activity");
                            finish();
                        } else {
                            Log.d("debug", "isReturn is false");
                            Log.d("debug", "Starting dashboard activity");
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "Incorrect credentials.", Toast.LENGTH_SHORT).show();
                    }
                }

                /**
                 * if response fails
                 * @param call
                 * @param t The error that is sent from the API
                 */
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Login failed due to error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}