package com.example.poste.activities;

import static com.example.poste.utils.ValidationUtils.validateEmail;

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

    /**
     * function that runs on creation of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView hyperlinkTextView = findViewById(R.id.hyperlinkTextViewToRegister);
        usernameField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);

        buttonLoginSubmit = findViewById(R.id.loginLoginbtn);

        /**
         * adds a hyperlink to redirect user to the login page
         */
        SpannableString spannable = new SpannableString(hyperlinkTextView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Start the SecondActivity when the link is clicked
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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
            MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
            Call<ResponseBody> call = apiService.loginUser(new LoginRequest(email, password));
            call.enqueue(new Callback<ResponseBody>() {
                /**
                 * Function that runs when response is returned by the API
                 * @param call
                 * @param response
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
                        // open dashboard activity
                        Intent dashboardIntent = new Intent(LoginActivity.this, IntroActivity.class);
                        startActivity(dashboardIntent);
                    }else {
                        Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                }

                /**
                 * if respose fails
                 * @param call
                 * @param t
                 */
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}