package com.example.poste.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The EditPostActivity class adds functionality to the activity_edit_post.xml layout
 *
 * The activity that calls this activity, do a .putExtra to pass the postId
 *
 * Example:
 * Intent editPostIntent = new Intent(FolderViewActivity.this, EditPostActivity.class);
 * finish();
 * editPostIntent.putExtra("postID",postId);
 * editPostIntent.putExtra("folderID",folderId);
 * startActivity(editPostIntent);
 */
class EditPostActivity : AppCompatActivity() {
    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)
        getSupportActionBar().hide()
        //        DebugUtils.logUserFoldersAndPosts(User.getUser());
        val postLink: EditText = findViewById(R.id.editTextPostLink)
        val postTitle: EditText = findViewById(R.id.editTextPostTitle)
        val postDescription: EditText = findViewById(R.id.editTextPostDescription)
        val postTags: EditText = findViewById(R.id.editTextPostTags)
        val buttonSaveChanges: Button = findViewById(R.id.buttonSaveChanges)
        val buttonCancelChanges: Button = findViewById(R.id.buttonCancelChanges)
        val currentPost: Post?
        val postId: String = getIntent().getStringExtra("postID")
        if (postId == null) {
            currentPost = null
            Log.e("Error", "EditPostActivity onCreate: Post not found")
        } else {
            currentPost = User.getUser().getPost(postId)
        }
        if (currentPost == null) {
            Log.e("Error", "EditPostActivity onCreate: Post not found")
        } else {
            postTitle.setText(currentPost.getTitle())
            postDescription.setText(currentPost.getDescription())
            postLink.setText(currentPost.getUrl())
            var tagsNewText = ""
            for (s in currentPost.getTags()) {
                if (tagsNewText.length > 0) {
                    tagsNewText = "$tagsNewText, "
                }
                tagsNewText = tagsNewText + s
            }
            postTags.setText(tagsNewText)
        }
        buttonCancelChanges.setOnClickListener { view: View? -> finish() }
        buttonSaveChanges.setOnClickListener { view: View? ->
            if (currentPost != null) {
                val title: String = postTitle.getText().toString()
                val description: String = postDescription.getText().toString()
                val url: String = postLink.getText().toString()
                val tagsString: String = postTags.getText().toString()
                val apiService: MyApiService = RetrofitClient.getRetrofitInstance().create(
                    MyApiService::class.java
                )
                val call: Call<ResponseBody> = apiService.editPost(
                    User.getUser().getTokenHeaderString(),
                    postId,
                    EditPostRequest(title, description, url, tagsString)
                )
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful()) {
                            currentPost.setTitle(title)
                            currentPost.setDescription(description)
                            currentPost.setUrl(url)
                            currentPost.setTags(Post.parseTags(tagsString))
                            Toast.makeText(
                                this@EditPostActivity,
                                getString(R.string.edit_post_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            val errorMessage: String = utils.parseError(response)
                            if (errorMessage != null) {
                                Toast.makeText(
                                    this@EditPostActivity,
                                    getString(R.string.error_message) + errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@EditPostActivity,
                                    getString(R.string.edit_failure),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@EditPostActivity,
                            getString(R.string.edit_failure),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                })
            }
        }
    }
}