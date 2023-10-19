package com.example.poste.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.adapters.FolderAdapter;
import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.Post;
import com.example.poste.api.poste.models.User;
import com.example.poste.http.FolderRequest;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The DashboardActivity class adds functionality to the activity_dashboard.xml layout
 */
public class DashboardActivity extends PActivity {
    private User currentUser;
    private List<Folder> userFolders;
    private RecyclerView folderRecyclerView;
    private FolderAdapter folderAdapter;
    public ImageView optionsView;
    public HashMap<String, Integer> folderIdNameMap = new HashMap<>();
    private MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState A bundle containing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Update app info
        // User.getUser().updateFoldersAndPosts();

        // Configure window settings for fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Set the activity layout
        setContentView(R.layout.activity_dashboard);

        // Prep vars
        currentUser = PosteApplication.getCurrentUser();
        optionsView = findViewById(R.id.Optionsbtn);
        folderRecyclerView = findViewById(R.id.folder_recycler_view);
        Button addButton = findViewById(R.id.dashboard_add_folder_btn);

        userFolders = currentUser.getFolders();
        for (Folder folder: userFolders) {
            folderIdNameMap.put(String.format("(%d) %s", folder.getId(), folder.getName()), folder.getId());
        }

        // Click listener for the options button
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

        // Fill folder view (Recycler View)
        folderRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        folderAdapter = new FolderAdapter(
                new FolderAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, Folder model) {
                        // Send to that folder's view
                        Intent intent = new Intent(DashboardActivity.this, FolderViewActivity.class );
                        intent.putExtra("folderId", model.getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, Folder model) {
                        // Prompt to share folder
                        Toast.makeText(DashboardActivity.this, getString(R.string.share_folder), Toast.LENGTH_LONG).show();
                    }
                },
                new ArrayList<>(userFolders)
        );
        folderRecyclerView.setAdapter(folderAdapter);
        registerForContextMenu(folderRecyclerView);
    }


    private void showCreateItemDialog() {
        // Find the [+] button on the dashboard
        Button addButton = findViewById(R.id.dashboard_add_folder_btn);

        // Create a PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, addButton);
        popupMenu.inflate(R.menu.menu_create_item);

        // Set a MenuItemClickListener to handle the selection
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_folder:
                        // Handle folder creation
                        showCreateFolderDialog();
                        return true;
                    case R.id.menu_post:
                        // Handle post creation
                        showCreatePostDialog();
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Show the PopupMenu
        popupMenu.show();
    }


    private void showCreatePostDialog() {
        // Inflate the layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_create_post, null);

        // Find the RadioGroup and EditText fields in the dialog
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        EditText editTextItemLink = dialogView.findViewById(R.id.editTextItemLink);
        Spinner spinnerNewPostFolder = dialogView.findViewById(R.id.spinnerNewPostFolder);

        // Setup for the dropdown menu
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<>(folderIdNameMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNewPostFolder.setAdapter(adapter);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Create Post");

        // Set the positive button (Create button) click listener
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the item name and link from the EditText fields
                String itemName = editTextItemName.getText().toString().trim();
                String itemLink = editTextItemLink.getText().toString().trim();
                String selectedFolderName = spinnerNewPostFolder.getSelectedItem().toString();
                Integer selectedFolderId = folderIdNameMap.get(selectedFolderName);

                if (itemName == null || itemName == "" || itemLink == null || itemLink == "") {
                    Toast.makeText(DashboardActivity.this, "Cannot create post, please enter name and link", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!itemLink.matches("^((http|https):\\/\\/)(www.)?[a-zA-Z0-9@:%._\\\\+~#?&/=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\\\+~#?&/=]*)$")) {
                    Toast.makeText(DashboardActivity.this, "Invalid Link", Toast.LENGTH_LONG).show();
                    return;
                }

                // Handle post creation logic
                try {
                    // Create the post in the API
                    // TODO: change to retrofit
                    int newPostID = API.addPost(itemName, itemLink, currentUser.getId());

                    // Update the post's folder in the API
                    // TODO: change to retrofit
                    API.addPostToFolder(newPostID, selectedFolderId);
                } catch (APIException e) {
                    throw new RuntimeException(e);
                }

                // For simplicity, let's just display a toast message with the post details
                Toast.makeText(DashboardActivity.this, "Post created:\nName: " + itemName + "\nLink: " + itemLink, Toast.LENGTH_LONG).show();
                dialog.dismiss();

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        // Set the negative button (Cancel button) click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog (do nothing)
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showCreateFolderDialog() {
        // Inflate the layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_create_folder, null);

        // Find the RadioGroup and EditText fields in the dialog
        EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Create Folder");

        // Set the positive button (Create button) click listener
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                // Get the item name and link from the EditText fields
                String itemName = editTextItemName.getText().toString().trim();

                // Handle folder creation logic
                    Call<ResponseBody> call = apiService.createFolder(new FolderRequest(itemName, currentUser.getId()));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject folder = new JSONObject(response.body().toString());
                                    userFolders.add(new Folder(folder.getInt("id"), folder.getString("title"), currentUser.getId(), new ArrayList<>(), new HashMap<>()));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Toast.makeText(DashboardActivity.this, "folder creation successful.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(DashboardActivity.this, "folder creation failed.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(DashboardActivity.this, "folder creation failed.", Toast.LENGTH_LONG).show();
                        }
                    });
                    userFolders = currentUser.getFolders();
                    folderAdapter.notifyItemInserted(userFolders.size() - 1);


                // For simplicity, let's just display a toast message with the post details
                Toast.makeText(DashboardActivity.this, "Folder created", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        // Set the negative button (Cancel button) click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog (do nothing)
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Folder folder = folderAdapter.getLocalDataSetItem();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_folder:
                intent = new Intent(DashboardActivity.this, EditFolderActivity.class );
                intent.putExtra("folderId", folder.getId());
                intent.putExtra("folderName", folder.getName());
                intent.putExtra("folderShared", true);
                startActivity(intent);
                finish();
                break;
            case R.id.ctx_menu_share_folder:
                intent = new Intent(DashboardActivity.this, Shared_Folder.class);
                intent.putExtra("folderId", folder.getId());
                intent.putExtra("folderName", folder.getName());
                startActivity(intent);
                finish();
                break;
            case R.id.ctx_menu_delete_folder:
                    // Remove all access
                    HashMap<Integer, FolderAccess> folderUsers = folder.getUsers();
                    folderUsers.forEach((userId, folderAccess) -> {
                        try {
                            // TODO: change to retrofit
                            API.removeUserFromFolder(folder.getId(), userId);
                        } catch (APIException e) { }
                    });

                    // Delete the folder
                    Call<ResponseBody> call = apiService.deleteFolder(folder.getId());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                JSONObject res;
                                try {
                                    res = new JSONObject(response.body().toString());
                                    if (res.getBoolean("success")){
                                        Toast.makeText(DashboardActivity.this, "folder deleted successful.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(DashboardActivity.this, "folder deletion failed.", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Toast.makeText(DashboardActivity.this, "folder deletion failed.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(DashboardActivity.this, "folder deletion failed.", Toast.LENGTH_LONG).show();
                        }
                    });
                    userFolders.remove(folder);

                    // updates view
                    userFolders = currentUser.getFolders();
                    folderAdapter.notifyItemInserted(userFolders.size() - 1);
                break;
        }
        return true;
    }

}

