package com.example.poste.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.poste.utils.ValidationUtils.validatePassword
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * The AccountActivity class adds functionality to the activity_account.xml layout
 */
class AccountActivity : AppCompatActivity() {
    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure window settings
        val actionBar: ActionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.hide()
        }

        // Set the activity layout
        setContentView(R.layout.activity_account)

        // Prep vars
        val buttonSave: Button = findViewById(R.id.AccountSavebtn)
        val oldPasswordEditText: EditText = findViewById(R.id.AccountCurrentPasswordEditText)
        val newPasswordEditText: EditText = findViewById(R.id.AccountNewPasswordEnter)
        val newPasswordConfirmEditText: EditText = findViewById(R.id.AccountConfirmPasswordEnter)
        // Click listener for the save account button
        buttonSave.setOnClickListener { view: View? ->
            val oldPassword: String = oldPasswordEditText.getText().toString()
            val newPassword: String = newPasswordEditText.getText().toString()
            val newPasswordConfirm: String = newPasswordConfirmEditText.getText().toString()
            if (!validatePassword(this@AccountActivity, newPassword, newPasswordConfirm)) {
                return@setOnClickListener
            }
            val apiService: MyApiService =
                RetrofitClient.getRetrofitInstance().create(MyApiService::class.java)
            val call: Call<ResponseBody> = apiService.editUser(
                User.getUser().getTokenHeaderString(),
                EditAccountRequest(oldPassword, newPassword)
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful()) {
                        Toast.makeText(
                            this@AccountActivity,
                            getString(R.string.password_change_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        val jsonResponse: String
                        jsonResponse = try {
                            response.body().string()
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                        var jsonObject: JSONObject? = null
                        val token: String
                        try {
                            jsonObject = JSONObject(jsonResponse)
                            token = jsonObject.getJSONObject("result").getString("token")
                        } catch (e: JSONException) {
                            throw RuntimeException(e)
                        }
                        Log.d("debug", "Token: $token")
                        User.getUser().setToken(token)

                        // Send to dashboard page
                        val intent = Intent(this@AccountActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMessage: String?
                        try {
                            errorMessage =
                                JSONObject(response.errorBody()!!.string()).getString("error")
                            Log.e("Error", errorMessage)
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        } catch (e: JSONException) {
                            throw RuntimeException(e)
                        }
                        if (errorMessage != null) {
                            Toast.makeText(this@AccountActivity, errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                this@AccountActivity,
                                getString(R.string.password_change_failure),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@AccountActivity,
                        getString(R.string.password_change_failure),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}