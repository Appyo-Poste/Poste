package com.example.poste.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.poste.utils.ValidationUtils.validateEmail
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    var buttonLoginSubmit: Button? = null
    private var usernameField: EditText? = null
    private var passwordField: EditText? = null
    private var rememberMeCheckbox: CheckBox? = null

    /**
     * function that runs on creation of the activity
     * @param savedInstanceState A bundle containing the saved instance state
     */
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getSupportActionBar().hide()
        val hyperlinkTextView: TextView = findViewById(R.id.hyperlinkTextViewToRegister)
        usernameField = findViewById(R.id.editTextTextEmailAddress)
        passwordField = findViewById(R.id.editTextTextPassword)
        buttonLoginSubmit = findViewById(R.id.loginLoginbtn)
        val isReturn: Boolean = getIntent().getBooleanExtra(
            "return",
            false
        ) // if true, dont start dashboard activity, just finish this one
        Log.d("debug", "isReturn: $isReturn")

        // configure 'remember me' checkbox
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckBox)
        rememberMeCheckbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("sharedPreferences", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("rememberMe", isChecked)
            editor.apply()
            Log.d("debug", "rememberMe: $isChecked")
        })

        // adds a hyperlink to redirect user to the login page
        val spannable = SpannableString(hyperlinkTextView.text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Start the SecondActivity when the link is clicked
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
        spannable.setSpan(
            clickableSpan,
            0,
            hyperlinkTextView.text.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        hyperlinkTextView.setText(spannable)
        hyperlinkTextView.movementMethod = LinkMovementMethod.getInstance()
        /**
         * Function that runs when login button is pressed
         * Makes a request to the API and gets a response in return
         */
        buttonLoginSubmit!!.setOnClickListener { view: View? ->
            val email: String = usernameField.getText().toString()
            val password: String = passwordField.getText().toString()
            if (!validateEmail(this@LoginActivity, email)) {
                return@setOnClickListener
            }
            if (!validatePassword(this@LoginActivity, password)) {
                return@setOnClickListener
            }
            val apiService: MyApiService =
                RetrofitClient.getRetrofitInstance().create(MyApiService::class.java)
            val call: Call<ResponseBody> = apiService.loginUser(LoginRequest(email, password))
            call.enqueue(object : Callback<ResponseBody> {
                /**
                 * Function that runs when response is returned by the API
                 * @param call
                 * @param response Response returned by the API
                 */
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful()) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_successful),
                            Toast.LENGTH_SHORT
                        ).show()
                        try {
                            val jsonResponse: String = response.body().string()
                            val jsonObject = JSONObject(jsonResponse)
                            val token = jsonObject.getJSONObject("result").getString("token")
                            Log.d("debug", "Token: $token")
                            User.getUser().setToken(token)
                            val sharedPreferences: SharedPreferences =
                                getSharedPreferences("sharedPreferences", MODE_PRIVATE)
                            if (rememberMeCheckbox.isChecked()) {
                                sharedPreferences.edit().putString("token", token).apply()
                            } else {
                                sharedPreferences.edit().putString("token", "").apply()
                            }
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("token", token)
                            editor.apply()
                            User.getUser().setEmail(email)
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        } catch (err: JSONException) {
                            Log.e("Json Error", err.toString())
                        }
                        if (isReturn) {
                            Log.d("debug", "isReturn is true")
                            Log.d("debug", "Finishing activity, returning to previous activity")
                            finish()
                        } else {
                            Log.d("debug", "isReturn is false")
                            Log.d("debug", "Starting dashboard activity")
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_invalid_credentials),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                /**
                 * if response fails
                 * @param call
                 * @param t The error that is sent from the API
                 */
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}