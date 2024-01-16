package com.example.poste.activities

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FolderUserActivity : AppCompatActivity() {
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getSupportActionBar().hide()
        setContentView(R.layout.activity_folder_user)
        val folderID: String = getIntent().getStringExtra("folderId")
        val folderName: String = getIntent().getStringExtra("folderName")
        if (folderID == null || folderName == null) {
            Log.d("FolderUserDebug", "Folder ID or folder name is null")
            finish()
        }
        val folder: Folder = User.getUser().getFolder(folderID)
        val sharedUsers: List<String> = folder.getSharedUsers()
        val folderNameText: TextView = findViewById(R.id.folderNameText)
        folderNameText.text = folderName
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewUsers)
        val cl: UserAdapter.ClickListener = object : ClickListener() {
            fun onItemClick(position: Int, user: String) {
                Log.d("UserDebug", "Clicked: User " + (position + 1) + ": " + user)
                // unshare folder with user
                // get the email associated with this recycler view item
                val email = sharedUsers[position]
                val apiService: MyApiService = RetrofitClient.getRetrofitInstance().create(
                    MyApiService::class.java
                )
                val call: Call<ResponseBody> = apiService.updateFolderPermissions(
                    User.getUser().getTokenHeaderString(),
                    UpdateFolderPermissionsRequest(folderID, email, FolderAccess.NONE)
                )
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                this@FolderUserActivity,
                                getString(R.string.unshared_folder_user) + email,
                                Toast.LENGTH_SHORT
                            ).show()
                            sharedUsers.removeAt(position)
                            recyclerView.getAdapter().notifyItemRemoved(position)
                            recyclerView.getAdapter()
                                .notifyItemRangeChanged(position, sharedUsers.size)
                        } else {
                            Toast.makeText(
                                this@FolderUserActivity,
                                getString(R.string.error_message) + response.message(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@FolderUserActivity,
                            getString(R.string.communication_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

            fun onItemLongClick(position: Int, model: String) {
                Log.d("UserDebug", "Long Clicked: User " + (position + 1) + ": " + model)
            }
        }
        registerForContextMenu(recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        val adapter = UserAdapter(cl, sharedUsers)
        recyclerView.setAdapter(adapter)
    }
}