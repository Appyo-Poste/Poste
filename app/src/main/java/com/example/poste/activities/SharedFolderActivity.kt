package com.example.poste.activities

import android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The AccountActivity class adds functionality to the activity_shared_folder.xml layout
 */
class SharedFolderActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_shared_folder)
        val folderNameView: TextView = findViewById(R.id.textViewShareFolderName)
        val emailView: EditText = findViewById(R.id.editTextEmailToShareWith)
        val permissionsSpinner: Spinner = findViewById(R.id.share_folder_permissions_spinner)
        val permissionsSpinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_values,
            R.layout.simple_spinner_item
        )
        permissionsSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        permissionsSpinner.setAdapter(permissionsSpinnerAdapter)
        val intent: Intent = getIntent()
        folderNameView.setText(intent.getStringExtra("folderName"))
        val saveBtn: Button = findViewById(R.id.buttonSaveChanges)
        val cancelBtn: Button = findViewById(R.id.buttonCancelChanges)
        saveBtn.setOnClickListener { view: View? ->
            val permissionsSelectedAccessValue: String =
                permissionsSpinner.getSelectedItem().toString()
            try {
                val folderId: String = intent.getStringExtra("folderId")
                val email: String = emailView.getText().toString()
                var selectedAccess: FolderAccess? = null
                selectedAccess = when (permissionsSelectedAccessValue) {
                    "View Access" -> FolderAccess.VIEWER
                    "Edit Access" -> FolderAccess.EDITOR
                    "Full Access" -> FolderAccess.FULL_ACCESS
                    else -> FolderAccess.NONE
                }
                val apiService: MyApiService = RetrofitClient.getRetrofitInstance().create(
                    MyApiService::class.java
                )
                val call: Call<ResponseBody> = apiService.updateFolderPermissions(
                    User.getUser().getTokenHeaderString(),
                    UpdateFolderPermissionsRequest(folderId, email, selectedAccess)
                )
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                this@SharedFolderActivity,
                                getString(R.string.share_folder_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@SharedFolderActivity,
                                getString(R.string.error_message) + response.message(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@SharedFolderActivity,
                            getString(R.string.Edit_shared_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, R.string.internal_error, Toast.LENGTH_LONG).show()
            }
            finish()
        }
        cancelBtn.setOnClickListener { view: View? -> finish() }
    }
}