package com.example.poste.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The DashboardActivity class adds functionality to the activity_dashboard.xml layout
 */
class DashboardActivity : PActivity() {
    private var addButton: Button? = null
    private var searchButton: Button? = null
    private var folderRecyclerView: RecyclerView? = null
    private var folderAdapter: FolderAdapter? = null
    private var optionsView: ImageView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val apiService: MyApiService = RetrofitClient.getRetrofitInstance().create(
        MyApiService::class.java
    )
    private var updateCallback: UpdateCallback? = null
    private var dashboardViewEmptyText: TextView? = null
    protected fun onRestart() {
        super.onRestart()
        updateData()
    }

    private fun updateData() {
        dashboardViewEmptyText!!.visibility = View.GONE
        User.getUser().updateFoldersAndPosts(updateCallback)
    }

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureWindow()
        prepVars()
        setListeners()
        createUpdateCallback()
        User.getUser().updateFoldersAndPosts(updateCallback)
    }

    /**
     * Creates a callback to be used when the user's folders and posts are updated
     * (i.e. when the user's folders and posts are retrieved from the API)
     * If the callback already exists, this method does nothing.
     * It is a way to handle asynchronous code.
     * Since the updateFoldersAndPosts call happens off the main thread, we cannot expect
     * that it will finish in time for us to use the updated data in the onCreate method.
     * Instead, we define a callback that will be called when the update is finished.
     * This callback is passed to the updateFoldersAndPosts method, which will call it
     * when the update is finished saying it succeeded or failed.
     * We simply define what we will do if it succeeds or fails.
     */
    private fun createUpdateCallback() {
        if (updateCallback != null) {
            return
        }
        updateCallback = object : UpdateCallback() {
            /**
             * This defines what will happen if the update succeeds; what we want the application
             * to do in a success case.
             * Again, this is assuming that a User.getUser().updateFoldersAndPosts() call was made
             * and that the callback was passed to it; so, what do we want to do if the update
             * was successful?
             */
            fun onSuccess() {
                folderAdapter.setLocalDataSet(User.getUser().getFolders())
                folderAdapter.notifyDataSetChanged()
                if (User.getUser().getFolders().size() === 0) {
                    dashboardViewEmptyText!!.visibility = View.VISIBLE
                }
            }

            /**
             * This defines what will happen if the update fails; what we want the application
             * to do in a failure case (e.g. if the API call fails).
             * @param errorMessage The error message returned by the API call (or whatever we send
             * the callback to)
             */
            fun onError(errorMessage: String?) {
                Toast.makeText(
                    this@DashboardActivity,
                    getString(R.string.retrieve_error),
                    Toast.LENGTH_SHORT
                ).show()
                if (User.getUser().getFolders().size() === 0) {
                    dashboardViewEmptyText!!.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Sets the listeners for the buttons on the dashboard
     */
    private fun setListeners() {
        optionsView!!.setOnClickListener { view: View? ->
            // Send to Options activity
            val intent = Intent(this@DashboardActivity, OptionsActivity::class.java)
            startActivity(intent)
        }

        // Click listener for the add folder button
        addButton!!.setOnClickListener { v: View? ->
            // Show the create item dialog
            showCreateItemDialog()
        }

        // Sent to search activity
        searchButton!!.setOnClickListener { view: View? ->
            val sendToSearchActivity = Intent(this@DashboardActivity, SearchActivity::class.java)
            startActivity(sendToSearchActivity)
        }
    }

    /**
     * Prepares the variables used in the activity. Checks if they are null, and if they are,
     * initializes them. This saves us from resetting them every time the activity is restarted.
     */
    private fun prepVars() {
        if (dashboardViewEmptyText == null) {
            dashboardViewEmptyText = findViewById(R.id.dashboardViewEmptyText)
            dashboardViewEmptyText!!.visibility = View.GONE // hide by default
        }
        if (optionsView == null) {
            optionsView = findViewById(R.id.optionsbtn)
        }
        if (folderRecyclerView == null) {
            folderRecyclerView = findViewById(R.id.folder_recycler_view)
            folderRecyclerView.setLayoutManager(GridLayoutManager(this, FOLDERS_PER_ROW))
            folderAdapter = FolderAdapter(
                object : ClickListener() {
                    fun onItemClick(position: Int, model: Folder?) {
                        // Send to that folder's view
                        User.getUser().setSelectedFolder(model)
                        val intent = Intent(this@DashboardActivity, FolderViewActivity::class.java)
                        startActivity(intent)
                    }

                    fun onItemLongClick(position: Int, model: Folder?) {
                        // Prompt to share folder
                        Toast.makeText(
                            this@DashboardActivity,
                            getString(R.string.share_folder),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                ArrayList<Any?>(User.getUser().getFolders())
            )
            folderRecyclerView.setAdapter(folderAdapter)
            registerForContextMenu(folderRecyclerView)
        }
        if (addButton == null) {
            addButton = findViewById(R.id.dashboard_add_folder_btn)
        }
        if (searchButton == null) {
            searchButton = findViewById(R.id.dashboard_search_btn)
        }
    }

    /**
     * Configures the window
     */
    private fun configureWindow() {
        setContentView(R.layout.activity_dashboard)

        // Configure window settings
        val actionBar: ActionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.hide()
        }

        // Setup refresh
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout?
            swipeRefreshLayout.setOnRefreshListener {
                updateData()
                swipeRefreshLayout.setRefreshing(false)
            }
        }
    }

    private fun showCreateItemDialog() {
        // Find the [+] button on the dashboard
        val addButton: Button = findViewById(R.id.dashboard_add_folder_btn)

        // Create a PopupMenu
        val popupMenu = PopupMenu(this, addButton)
        popupMenu.inflate(R.menu.menu_create_item)

        // Set a MenuItemClickListener to handle the selection
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_folder -> {
                    // Handle folder creation
                    showCreateFolderDialog()
                    return@setOnMenuItemClickListener true
                }

                R.id.menu_post -> {
                    // Handle post creation
                    val intent = Intent(this@DashboardActivity, NewPostActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }

                else -> return@setOnMenuItemClickListener false
            }
        }
        // Show the PopupMenu
        popupMenu.show()
    }

    // Removed Create Post dialog in favor of NewPostActivity
    private fun showCreateFolderDialog() {
        // Inflate the layout for the dialog
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val dialogView: View = inflater.inflate(R.layout.layout_dialog_create_folder, null)

        // Find the RadioGroup and EditText fields in the dialog
        val editTextItemName: EditText = dialogView.findViewById<EditText>(R.id.editTextItemName)

        // Create the AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setTitle(getString(R.string.new_folder))

        // Set the positive button (Create button) click listener
        builder.setPositiveButton(getString(R.string.create)) { dialog, which ->


            // Get the item name and link from the EditText fields
            val itemName: String = editTextItemName.getText().toString().trim { it <= ' ' }

            // Handle folder creation logic
            val call: Call<ResponseBody> = apiService.createFolder(
                User.getUser().getTokenHeaderString(),
                FolderRequest(itemName)
            )
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.isSuccessful()) {
                        // Display success message, then restart the activity
                        Toast.makeText(
                            this@DashboardActivity,
                            getString(R.string.folder_creation_success),
                            Toast.LENGTH_LONG
                        ).show()
                        updateData()
                    } else {
                        Toast.makeText(
                            this@DashboardActivity,
                            getString(R.string.folder_creation_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(
                        this@DashboardActivity,
                        getString(R.string.folder_creation_failed),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
            dialog.dismiss()
        }

        // Set the negative button (Cancel button) click listener
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            // Dismiss the dialog (do nothing)
            dialog.dismiss()
        }

        // Show the AlertDialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // menu item select listener
    fun onContextItemSelected(item: MenuItem): Boolean {
        val folder: Folder = folderAdapter.getLocalDataSetItem()
        val intent: Intent
        when (item.itemId) {
            R.id.ctx_menu_edit_folder -> {
                intent = Intent(this@DashboardActivity, EditFolderActivity::class.java)
                intent.putExtra("folderId", folder.getId())
                intent.putExtra("folderName", folder.getTitle())
                intent.putExtra("folderShared", true)
                startActivity(intent)
            }

            R.id.ctx_menu_share_folder -> {
                intent = Intent(this@DashboardActivity, SharedFolderActivity::class.java)
                intent.putExtra("folderId", folder.getId())
                intent.putExtra("folderName", folder.getTitle())
                startActivity(intent)
            }

            R.id.ctx_menu_view_shares -> {
                intent = Intent(this@DashboardActivity, FolderUserActivity::class.java)
                intent.putExtra("folderId", folder.getId())
                intent.putExtra("folderName", folder.getTitle())
                startActivity(intent)
            }

            R.id.ctx_menu_delete_folder -> {
                // Delete the folder
                val call: Call<ResponseBody> = apiService.deleteFolder(
                    User.getUser().getTokenHeaderString(),
                    folder.getId()
                )
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                this@DashboardActivity,
                                getString(R.string.folder_delete_success),
                                Toast.LENGTH_LONG
                            ).show()
                            updateData()
                        } else {
                            val error: String = utils.parseError(response)
                            if (error != null) {
                                Toast.makeText(
                                    this@DashboardActivity,
                                    getString(R.string.folder_delete_failed_message) + error,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@DashboardActivity,
                                    getString(R.string.folder_delete_failed),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@DashboardActivity,
                            getString(R.string.folder_delete_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
        return true
    }

    companion object {
        private const val FOLDERS_PER_ROW = 3
    }
}