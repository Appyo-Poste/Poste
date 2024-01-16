package com.example.poste.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.poste.utils.ValidationUtils.validateEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private var emailEditText: EditText? = null
    private var firstNameText: EditText? = null
    private var lastNameText: EditText? = null
    private var passwordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var registerButton: Button? = null
    private var backButton: Button? = null
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getSupportActionBar().hide()
        emailEditText = findViewById(R.id.editTextEmail)
        firstNameText = findViewById(R.id.editTextFirstName)
        lastNameText = findViewById(R.id.editTextLastName)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        registerButton = findViewById(R.id.buttonRegister)
        backButton = findViewById(R.id.buttonBack)
        registerButton!!.setOnClickListener(View.OnClickListener {
            // Retrieve the values
            val email: String = emailEditText.getText().toString()
            val firstName: String = firstNameText.getText().toString().trim { it <= ' ' }
            val lastName: String = lastNameText.getText().toString().trim { it <= ' ' }
            val password: String = passwordEditText.getText().toString()
            val confirmPassword: String = confirmPasswordEditText.getText().toString()
            if (!validateEmail(this@RegisterActivity, email)) {
                return@OnClickListener
            }
            if (!validateNames(this@RegisterActivity, firstName, lastName)) {
                return@OnClickListener
            }
            if (!validatePassword(this@RegisterActivity, password, confirmPassword)) {
                return@OnClickListener
            }
            val apiService: MyApiService = RetrofitClient.getRetrofitInstance().create(
                MyApiService::class.java
            )
            val call: Call<ResponseBody> =
                apiService.registerUser(RegisterRequest(email, firstName, lastName, password))
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.isSuccessful()) {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.register_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        // open dashboard activity
                        val dashboardIntent =
                            Intent(this@RegisterActivity, IntroActivity::class.java)
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.register_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.register_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        })
        backButton!!.setOnClickListener { v: View? ->
            // Simulate a back press; go back to LandingActivity
            onBackPressed()
        }
    }
}