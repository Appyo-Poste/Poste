package com.example.poste.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.poste.R;
import com.example.poste.adapters.FolderAdapter;
import com.example.poste.callbacks.UpdateCallback;
import com.example.poste.http.FolderRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.models.Folder;
import com.example.poste.models.User;
import com.example.poste.utils.utils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The DashboardActivity class adds functionality to the activity_dashboard.xml layout
 */
public class DashboardActivity extends PActivity {
    private static final int FOLDERS_PER_ROW = 3;
    private Button addButton;
    private Button searchButton;
    private RecyclerView folderRecyclerView;
    private FolderAdapter folderAdapter;
    private ImageView optionsView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
    private UpdateCallback updateCallback;
    private TextView dashboardViewEmptyText;
    
    @Override
    protected void onRestart() {
        super.onRestart();
        updateData();
    }

    private void updateData() {
        dashboardViewEmptyText.setVisibility(View.GONE);
        User.getUser().updateFoldersAndPosts(updateCallback);
    }

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureWindow();
        prepVars();
        setListeners();
        createUpdateCallback();
        User.getUser().updateFoldersAndPosts(updateCallback);
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
    private void createUpdateCallback() {
        if (updateCallback != null) {
            return;
        }
        updateCallback = new UpdateCallback() {
            /**
             * This defines what will happen if the update succeeds; what we want the application
             * to do in a success case.
             * Again, this is assuming that a User.getUser().updateFoldersAndPosts() call was made
             * and that the callback was passed to it; so, what do we want to do if the update
             * was successful?
             */
            @Override
            public void onSuccess() {
                folderAdapter.setLocalDataSet(User.getUser().getFolders());
                folderAdapter.notifyDataSetChanged();
                if (User.getUser().getFolders().size() == 0) {
                    dashboardViewEmptyText.setVisibility(View.VISIBLE);
                }
            }

            /**
             * This defines what will happen if the update fails; what we want the application
             * to do in a failure case (e.g. if the API call fails).
             * @param errorMessage The error message returned by the API call (or whatever we send
             *                     the callback to)
             */
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(
                        DashboardActivity.this,
                        getString(R.string.retrieve_error),
                        Toast.LENGTH_SHORT
                ).show();
                if (User.getUser().getFolders().size() == 0) {
                    dashboardViewEmptyText.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    /**
     * Sets the listeners for the buttons on the dashboard
     */
    private void setListeners() {
        optionsView.setOnClickListener(view -> {
            // Send to Options activity
            Intent intent = new Intent(DashboardActivity.this, OptionsActivity.class);
            startActivity(intent);
        });

        // Click listener for the add folder button
        addButton.setOnClickListener(v -> {
            // Show the create item dialog
            showCreateItemDialog();
        });

        // Sent to search activity
        searchButton.setOnClickListener(view -> {
            Intent sendToSearchActivity = new Intent(DashboardActivity.this, SearchActivity.class);
            startActivity(sendToSearchActivity);
        });
    }

    /**
     * Prepares the variables used in the activity. Checks if they are null, and if they are,
     * initializes them. This saves us from resetting them every time the activity is restarted.
     */
    private void prepVars() {
        if (dashboardViewEmptyText == null) {
            dashboardViewEmptyText = findViewById(R.id.dashboardViewEmptyText);
            dashboardViewEmptyText.setVisibility(View.GONE); // hide by default
        }
        if (optionsView == null) {
            optionsView = findViewById(R.id.optionsbtn);
        }
        if (folderRecyclerView == null) {
            folderRecyclerView = findViewById(R.id.folder_recycler_view);
            folderRecyclerView.setLayoutManager(new GridLayoutManager(this, FOLDERS_PER_ROW));
            folderAdapter = new FolderAdapter(
                    new FolderAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, Folder model) {
                            // Send to that folder's view
                            User.getUser().setSelectedFolder(model);
                            Intent intent = new Intent(DashboardActivity.this, FolderViewActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(int position, Folder model) {
                            // Prompt to share folder
                            Toast.makeText(DashboardActivity.this, getString(R.string.share_folder), Toast.LENGTH_LONG).show();
                        }
                    },
                    new ArrayList<>(User.getUser().getFolders())
            );
            folderRecyclerView.setAdapter(folderAdapter);
            registerForContextMenu(folderRecyclerView);
        }
        if (addButton == null) {
            addButton = findViewById(R.id.dashboard_add_folder_btn);
        }
        if (searchButton == null) {
            searchButton = findViewById(R.id.dashboard_search_btn);
        }
    }

    /**
     * Configures the window
     */
    private void configureWindow() {
        setContentView(R.layout.activity_dashboard);

        // Configure window settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Setup refresh
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                updateData();
                swipeRefreshLayout.setRefreshing(false);
            });
        }
    }

    private void showCreateItemDialog() {
        // Find the [+] button on the dashboard
        Button addButton = findViewById(R.id.dashboard_add_folder_btn);

        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, addButton);
        popupMenu.inflate(R.menu.menu_create_item);

        // Set a MenuItemClickListener to handle the selection
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_folder:
                    // Handle folder creation
                    showCreateFolderDialog();
                    return true;
                case R.id.menu_post:
                    // Handle post creation
                    Intent intent = new Intent(DashboardActivity.this, NewPostActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return false;
            }
        });
        // Show the PopupMenu
        popupMenu.show();
    }

    // Removed Create Post dialog in favor of NewPostActivity

    private void showCreateFolderDialog() {
        // Inflate the layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_create_folder, null);

        // Find the RadioGroup and EditText fields in the dialog
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle(getString(R.string.new_folder));

        // Set the positive button (Create button) click listener
        builder.setPositiveButton(getString(R.string.create), (dialog, which) -> {


            // Get the item name and link from the EditText fields
            String itemName = editTextItemName.getText().toString().trim();

            // Handle folder creation logic
            Call<ResponseBody> call = apiService.createFolder(
                    User.getUser().getTokenHeaderString(),
                    new FolderRequest(itemName)
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Display success message, then restart the activity
                        Toast.makeText(
                                DashboardActivity.this,
                                getString(R.string.folder_creation_success),
                                Toast.LENGTH_LONG
                        ).show();
                        updateData();
                    } else {
                        Toast.makeText(
                                DashboardActivity.this,
                                getString(R.string.folder_creation_failed),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(
                            DashboardActivity.this,
                            getString(R.string.folder_creation_failed),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
            dialog.dismiss();
        });

        // Set the negative button (Cancel button) click listener
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            // Dismiss the dialog (do nothing)
            dialog.dismiss();
        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Folder folder = folderAdapter.getLocalDataSetItem();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_folder:
                intent = new Intent(DashboardActivity.this, EditFolderActivity.class);
                intent.putExtra("folderId", folder.getId());
                intent.putExtra("folderName", folder.getTitle());
                intent.putExtra("folderShared", true);
                startActivity(intent);
                break;
            case R.id.ctx_menu_share_folder:
                intent = new Intent(DashboardActivity.this, SharedFolderActivity.class);
                intent.putExtra("folderId", folder.getId());
                intent.putExtra("folderName", folder.getTitle());
                startActivity(intent);
                break;
            case R.id.ctx_menu_view_shares:
                intent = new Intent(DashboardActivity.this, FolderUserActivity.class);
                intent.putExtra("folderId", folder.getId());
                intent.putExtra("folderName", folder.getTitle());
                startActivity(intent);
                break;
            case R.id.ctx_menu_delete_folder:
                // Delete the folder
                Call<ResponseBody> call = apiService.deleteFolder(
                        User.getUser().getTokenHeaderString(),
                        folder.getId()
                );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DashboardActivity.this,
                                    getString(R.string.folder_delete_success),
                                    Toast.LENGTH_LONG).show();
                            updateData();
                        } else {
                            String error = utils.parseError(response);
                            if (error != null) {
                                Toast.makeText(
                                        DashboardActivity.this,
                                        getString(R.string.folder_delete_failed_message) + error,
                                        Toast.LENGTH_LONG
                                ).show();
                            } else {
                                Toast.makeText(
                                        DashboardActivity.this,
                                        getString(R.string.folder_delete_failed),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(
                                DashboardActivity.this,
                                getString(R.string.folder_delete_failed),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
                break;
        }
        return true;
    }
}
