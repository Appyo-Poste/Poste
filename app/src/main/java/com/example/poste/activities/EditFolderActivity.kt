package com.example.poste.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The EditFolderActivity class adds functionality to the activity_edit_folder_v2.xml layout
 */
@SuppressLint("UseSwitchCompatOrMaterialCode")
class EditFolderActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_edit_folder)

        // Prep vars
        val intent: Intent = getIntent()
        val cancelBtn: Button = findViewById(R.id.buttonCancelChanges)
        val saveBtn: Button = findViewById(R.id.buttonSaveChanges)
        val folderNameView: EditText = findViewById(R.id.editTextNameOfFolder)

        // Set text and checked
        folderNameView.setText(intent.getStringExtra("folderName"))

        // Save button push
        saveBtn.setOnClickListener { view: View? ->
            try {
                val folderId: String = intent.getStringExtra("folderId")
                val currentFolder: Folder = User.getUser().getFolder(folderId)
                val title: String = folderNameView.getText().toString()
                val apiService: MyApiService = RetrofitClient.getRetrofitInstance().create(
                    MyApiService::class.java
                )
                val call: Call<ResponseBody> = apiService.editFolder(
                    User.getUser().getTokenHeaderString(),
                    folderId,
                    EditFolderRequest(title)
                )
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful()) {
                            currentFolder.setTitle(title)
                            Toast.makeText(
                                this@EditFolderActivity,
                                getString(R.string.edit_folder_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@EditFolderActivity,
                                getString(R.string.error_message) + response.message(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@EditFolderActivity,
                            getString(R.string.edit_failure),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                })
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        // Cancel button push
        cancelBtn.setOnClickListener { view: View? -> finish() }
    }
}