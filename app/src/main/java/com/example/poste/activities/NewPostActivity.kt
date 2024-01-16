package com.example.poste.activities

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPostActivity : AppCompatActivity() {
    private var newPostIntent: Intent? = null
    private var buttonSaveChanges: Button? = null
    private var buttonCancelChanges: Button? = null
    private var chooseFolderSpinner: Spinner? = null
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        getSupportActionBar().hide()
        newPostIntent = getIntent()
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges)
        buttonCancelChanges = findViewById(R.id.buttonCancelChanges)
        chooseFolderSpinner = findViewById(R.id.chooseFolderSpinner)
        buttonCancelChanges!!.setOnClickListener { v: View? ->
            Log.d("NewPostActivity", "Cancel changes clicked")
            finish()
        }
        if (!User.getUser().isLoggedIn()) {
            // user needs to login
            Log.d("NewPostActivity", "User needs to login")
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.putExtra("return", true)
            startActivity(loginIntent)
            // when the user logs in, LoginActivity returns and onResume will be called
        } else {
            Log.d("NewPostActivity", "User is logged in")
            Log.d("NewPostActivity", "User token: " + User.getUser().getToken())
            handleNewPost()
        }
    }

    protected fun onResume() {
        super.onResume()
        Log.d("NewPostActivity", "onResume called; returning from LoginActivity?")
        val callback: UpdateCallback = object : UpdateCallback() {
            fun onSuccess() {
                Log.d("NewPostActivity", "Retrieved user data")
                handleNewPost()
            }

            fun onError(errorMessage: String) {
                Log.d("NewPostActivity", "Error retrieving user data: $errorMessage")
                handleNewPost()
            }
        }
        User.getUser().updateFoldersAndPosts(callback)
    }

    private fun handleNewPost() {
        var sharedText: String? = null
        if (newPostIntent != null && newPostIntent.getAction() != null && newPostIntent.getAction() == Intent.ACTION_SEND && newPostIntent.getType() != null && newPostIntent.getType() == "text/plain" && newPostIntent.getStringExtra(
                Intent.EXTRA_TEXT
            ) != null
        ) {
            sharedText = newPostIntent.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            Log.d("NewPostActivity", "No intent to pre-fill post")
        }
        if (sharedText != null) {
            val linkEditText: EditText = findViewById(R.id.editTextPostLink)
            linkEditText.setText(sharedText)
        } else {
            Log.d("NewPostActivity", "No shared text")
        }
        val folders: MutableList<Folder> = ArrayList<Folder>()
        folders.add(
            Builder()
                .setTitle(getString(R.string.folder_selection))
                .setId("0")
                .build()
        )
        // setup folder spinner
        folders.addAll(User.getUser().getFolders())
        val adapter: ArrayAdapter<Folder> =
            ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, folders)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        chooseFolderSpinner.setAdapter(adapter)
        // set default folder, if applicable
        val defaultFolder: String = newPostIntent.getStringExtra("default")
        if (defaultFolder != null) {
            for (i in folders.indices) {
                if (folders[i].getTitle().equals(defaultFolder)) {
                    chooseFolderSpinner.setSelection(i)
                    break
                }
            }
        }
        buttonSaveChanges!!.setOnClickListener { v: View? ->
            val folder_id: String = (chooseFolderSpinner.getSelectedItem() as Folder).getId()
            if (folder_id == "0") {
                Log.d("NewPostActivity", "Default folder (0) selected")
                Toast.makeText(
                    this,
                    getString(R.string.error_no_folder_selected),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("NewPostActivity", "Folder id: $folder_id")
                val title: String =
                    (findViewById(R.id.editTextPostTitle) as EditText).getText().toString()
                val url: String =
                    (findViewById(R.id.editTextPostLink) as EditText).getText().toString()
                val description: String =
                    (findViewById(R.id.editTextPostDescription) as EditText).getText().toString()
                val tags: String =
                    (findViewById(R.id.editTextPostTags) as EditText).getText().toString()
                val request = CreatePostRequest(
                    title,
                    description,
                    url,
                    folder_id,
                    tags
                )
                val apiService: MyApiService = RetrofitClient.getRetrofitInstance().create(
                    MyApiService::class.java
                )
                val call: Call<ResponseBody> =
                    apiService.createPost(User.getUser().getTokenHeaderString(), request)
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                this@NewPostActivity,
                                getString(R.string.post_creation_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@NewPostActivity,
                                getString(R.string.post_creation_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(
                                "NewPostActivity",
                                "Failed to create post: " + response.code() + " " + response.message()
                            )
                        }
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@NewPostActivity,
                            getString(R.string.post_creation_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                })
            }
        }
    }
}