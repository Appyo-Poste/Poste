package com.example.poste.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar

/**
 * The OptionsActivity class adds functionality to the activity_options.xml layout
 */
class OptionsActivity : AppCompatActivity() {
    var Signoutbtn: Button? = null

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
        setContentView(R.layout.activity_options)
        Signoutbtn = findViewById(R.id.Signoutbtn)
        Signoutbtn!!.setOnClickListener { view: View? ->
            User.getUser().logout()
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("sharedPreferences", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("rememberMe", false)
            editor.putString("token", "")
            editor.apply()
            val intent = Intent(getApplicationContext(), IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        val accountSettings: Button = findViewById(R.id.options_accounts_btn)
        accountSettings.setOnClickListener { view: View? ->
            val accountSettingsIntent = Intent(this@OptionsActivity, AccountActivity::class.java)
            startActivity(accountSettingsIntent)
            finish()
        }
    }
}