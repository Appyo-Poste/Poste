package com.example.poste.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * The FolderViewActivity class adds functionality to the activity_folder_view.xml layout.
 * This class governs the page where users can view, use, and edit the posts contained within a
 * selected folder.
 */
class FolderViewActivity : AppCompatActivity() {
    private var updateCallback: UpdateCallback? = null
    private var postAdapter: PostAdapter? = null
    private var postRecyclerView: RecyclerView? = null
    private var emptyText: TextView? = null
    private var folderName: TextView? = null
    private var newBut: Button? = null
    private var settingsBut: ImageView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    fun onBackPressed() {
        User.getUser().setSelectedPost(null)
        User.getUser().setSelectedFolder(null)
        super.onBackPressed()
    }

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        User.getUser().setSelectedPost(null)
        configureWindow()
        prepVars()
        setListeners()
        updateData()
    }

    protected fun onResume() {
        super.onResume()
        updateData()
    }

    private fun setListeners() {
        newBut!!.setOnClickListener { view: View? ->
            User.getUser().setSelectedPost(null)
            val intent = Intent(this@FolderViewActivity, NewPostActivity::class.java)
            intent.putExtra("default", User.getUser().getSelectedFolder().getTitle())
            startActivity(intent)
        }
        settingsBut!!.setOnClickListener { view: View? ->
            val intent = Intent(this@FolderViewActivity, EditFolderActivity::class.java)
            intent.putExtra("folderId", User.getUser().getSelectedFolder().getId())
            intent.putExtra("folderName", User.getUser().getSelectedFolder().getTitle())
            startActivity(intent)
        }
    }

    private fun prepVars() {
        emptyText = findViewById(R.id.folderViewEmptyText)
        emptyText!!.visibility = View.GONE // Hide empty text until data is loaded
        folderName = findViewById(R.id.folderNameText)
        folderName!!.visibility = View.INVISIBLE // Hide folder name until data is loaded
        // reset selected folder
        User.getUser()
            .setSelectedFolder(User.getUser().getFolder(User.getUser().getSelectedFolder().getId()))
        // Set folder name in title bar
        folderName.setText(User.getUser().getSelectedFolder().getTitle())
        folderName!!.visibility = View.VISIBLE // Show folder name now that data is loaded
        newBut = findViewById(R.id.newPost)
        settingsBut = findViewById(R.id.folderSettings)
        postRecyclerView = findViewById(R.id.posts_recycler_view)
        postAdapter = PostAdapter(
            object : ClickListener() {
                fun onItemClick(position: Int, post: Post?) {
                    User.getUser().setSelectedPost(post)
                    try {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(User.getUser().getSelectedPost().getUrl())
                        )
                        startActivity(browserIntent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@FolderViewActivity,
                            R.string.internal_error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                fun onItemLongClick(position: Int, post: Post?) {
                    User.getUser().setSelectedPost(post)
                }
            },
            User.getUser().getSelectedFolder().getPosts()
        )
        postRecyclerView.setAdapter(postAdapter)
        postRecyclerView.setLayoutManager(LinearLayoutManager(this@FolderViewActivity))
        updateCallback = object : UpdateCallback() {
            fun onSuccess() {
                // Fill post view (Recycler View)
                User.getUser().setSelectedFolder(
                    User.getUser().getFolder(User.getUser().getSelectedFolder().getId())
                )
                postAdapter.setLocalDataSet(User.getUser().getSelectedFolder().getPosts())
                postAdapter.notifyDataSetChanged()
                val newPosts: List<Post> = User.getUser().getSelectedFolder().getPosts()
                for (post in newPosts) {
                    Log.d("FolderViewActivity", "Post: " + post.getTitle())
                }
                if (User.getUser().getSelectedFolder().getPosts().size() > 0) {
                    emptyText!!.visibility = View.GONE
                } else {
                    emptyText!!.visibility = View.VISIBLE
                }
            }

            fun onError(errorMessage: String?) {
                Toast.makeText(
                    this@FolderViewActivity,
                    getString(R.string.folder_retrieve_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun configureWindow() {
        // Configure window settings
        val actionBar: ActionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.hide()
        }

        // Set the activity layout
        setContentView(R.layout.activity_folder_view)

        // Setup refresh
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout?
            swipeRefreshLayout.setOnRefreshListener {
                updateData()
                swipeRefreshLayout.setRefreshing(false)
            }
        }
    }

    fun onContextItemSelected(item: MenuItem): Boolean {
        val post: Post = postAdapter.getLocalDataSetItem()
        var intent: Intent? = null
        when (item.itemId) {
            R.id.ctx_menu_edit_post -> {
                User.getUser().setSelectedPost(post)
                if (User.getUser().getSelectedPost() != null) {
                    intent = Intent(this@FolderViewActivity, EditPostActivity::class.java)
                    intent.putExtra("postID", post.getId())
                    intent.putExtra("folderID", User.getUser().getSelectedFolder().getId())
                    startActivity(intent)
                }
            }

            R.id.ctx_menu_delete_post -> {
                User.getUser().setSelectedPost(post)
                if (User.getUser().getSelectedPost() != null) {
                    val postDeletionCallback: PostDeletionCallback =
                        object : PostDeletionCallback() {
                            fun onSuccess() {
                                Toast.makeText(
                                    this@FolderViewActivity,
                                    R.string.post_deleted,
                                    Toast.LENGTH_LONG
                                ).show()
                                updateData()
                            }

                            fun onError(errorMessage: String?) {
                                Toast.makeText(
                                    this@FolderViewActivity,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                                recreate()
                            }
                        }
                    User.getUser().deletePostFromServer(
                        User.getUser().getSelectedPost(),
                        postDeletionCallback
                    )
                }
            }
        }
        return true
    }

    private fun updateData() {
        emptyText!!.visibility = View.GONE // Hide empty text until data is loaded
        User.getUser().updateFoldersAndPosts(updateCallback)
    }
}