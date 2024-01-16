package com.example.poste.activities

import android.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Arrays

/**
 * Search activity for application
 * Allows user to search for posts across folders
 */
class SearchActivity : AppCompatActivity() {
    /**
     * Fields users can search by
     */
    private enum class SEARCH_FIELD {
        LINK, TITLE, DESCRIPTION, TAG
    }

    private var postAdapter: PostAdapter? = null
    var searchBtn: Button? = null
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        getSupportActionBar().hide()

        // prepare variables
        searchBtn = findViewById(R.id.search_go_button)
        val searchByEditText: EditText = findViewById(R.id.editTextSearchBy)
        val chooseSearchField: Spinner = findViewById(R.id.chooseFieldSpinner)
        val showPosts: RecyclerView = findViewById(R.id.posts_recycler_view)
        val emptyText: TextView = findViewById(R.id.searchViewEmptyText)
        emptyText.visibility = View.GONE

        // setup search field spinner
        val searchFields: MutableList<String> = ArrayList()
        searchFields.addAll(Arrays.asList(getResources().getStringArray(R.array.spinner_items)))
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, searchFields)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        chooseSearchField.setAdapter(adapter)

        // when Go! button is clicked
        searchBtn!!.setOnClickListener { v: View? ->
            val searchField = chooseSearchField.getSelectedItem() as String
            val searchBy: String = searchByEditText.getText().toString()
            if (searchField == searchFields[0]) {
                Log.d("SearchActivity", "Default selected")
                Toast.makeText(
                    this,
                    getString(R.string.select_search_field),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (searchBy.isBlank()) {
                    Toast.makeText(
                        this,
                        getString(R.string.search_field_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(
                        "SearchActivity",
                        Integer.toString(chooseSearchField.getSelectedItemPosition())
                    )
                    Log.d("SearchActivity", searchBy)
                    val searchResultList: List<Post> = searchResult(
                        SEARCH_FIELD.values()[chooseSearchField.getSelectedItemPosition() - 1],
                        searchBy
                    )
                    // Fill post view (Recycler View)
                    if (searchResultList.size == 0) {
                        emptyText.visibility = View.VISIBLE
                        showPosts.setVisibility(View.GONE)
                    } else {
                        emptyText.visibility = View.GONE
                        showPosts.setVisibility(View.VISIBLE)
                        showPosts.setLayoutManager(LinearLayoutManager(this@SearchActivity))
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
                                            this@SearchActivity,
                                            R.string.internal_error,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }

                                fun onItemLongClick(position: Int, post: Post?) {
                                    User.getUser().setSelectedPost(post)
                                }
                            },
                            searchResult(
                                SEARCH_FIELD.values()[chooseSearchField.getSelectedItemPosition() - 1],
                                searchBy
                            )
                        )
                        showPosts.setAdapter(postAdapter)
                    }
                }
            }
        }
    }

    /**
     * helper function to generate a list of posts the meet query
     * @param field to search by
     * @param queryString criteria
     * @return List of Posts that meet criteria
     */
    private fun searchResult(field: SEARCH_FIELD, queryString: String): List<Post> {
        val searchResultList: MutableList<Post> = ArrayList<Post>()
        for (f in User.getUser().getFolders()) {
            for (p in f.getPosts()) {
                when (field) {
                    SEARCH_FIELD.LINK -> if (p.getUrl().contains(queryString)) {
                        searchResultList.add(p)
                    }

                    SEARCH_FIELD.TITLE -> if (p.getTitle().contains(queryString)) {
                        searchResultList.add(p)
                    }

                    SEARCH_FIELD.DESCRIPTION -> if (p.getDescription().contains(queryString)) {
                        searchResultList.add(p)
                    }

                    SEARCH_FIELD.TAG -> if (p.getTags().contains(queryString)) {
                        searchResultList.add(p)
                    }

                    else -> {}
                }
            }
        }
        return searchResultList
    }

    /**
     * On resume run the search again to update the list
     */
    protected fun onResume() {
        super.onResume()
        searchBtn!!.callOnClick()
    }

    fun onContextItemSelected(item: MenuItem): Boolean {
        val post: Post = postAdapter.getLocalDataSetItem()
        when (item.itemId) {
            R.id.ctx_menu_edit_post -> {
                User.getUser().setSelectedPost(post)
                if (User.getUser().getSelectedPost() != null) {
                    val intent = Intent(this@SearchActivity, EditPostActivity::class.java)
                    intent.putExtra("postID", post.getId())
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
                                    this@SearchActivity,
                                    R.string.post_deleted,
                                    Toast.LENGTH_LONG
                                ).show()
                                recreate()
                            }

                            fun onError(errorMessage: String?) {
                                Toast.makeText(this@SearchActivity, errorMessage, Toast.LENGTH_LONG)
                                    .show()
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
}