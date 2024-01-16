package com.example.poste.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar

/**
 * The IntroActivity class adds functionality to the activity_intro.xml layout
 * This is the first page the user sees when they open the app
 */
class IntroActivity : AppCompatActivity() {
    protected fun onResume() {
        super.onResume()
        val user: User = User.getUser()
        if (!user.getToken().equals("")) {
            val intent = Intent(this@IntroActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user: User = User.getUser()
        if (user.getToken().isEmpty()) {
            val rememberMe: Boolean = getSharedPreferences(
                "sharedPreferences",
                Context.MODE_PRIVATE
            ).getBoolean("rememberMe", false)
            if (rememberMe) {
                val prefToken: String = getSharedPreferences(
                    "sharedPreferences",
                    Context.MODE_PRIVATE
                ).getString("token", "")
                if (!prefToken.isEmpty()) {
                    user.setToken(prefToken)
                }
            }
        }
        if (!user.getToken().isEmpty()) {
            val intent = Intent(this@IntroActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configure window settings
        val actionBar: ActionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.hide()
        }

        // Set the activity layout
        setContentView(R.layout.activity_intro)

        // Prep vars
        val buttonRegister: Button = findViewById(R.id.ACCreateAccountbtn2)
        val buttonLogin: Button = findViewById(R.id.ACCLoginbtn)

        // Click listener for the register button -- takes user to register page (RegisterActivity)
        buttonRegister.setOnClickListener { view: View? ->
            val intent = Intent(this@IntroActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Click listener for the login button -- takes user to login page (LoginActivity)
        buttonLogin.setOnClickListener { view: View? ->
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}