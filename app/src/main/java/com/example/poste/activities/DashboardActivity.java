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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
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
import com.example.poste.api.poste.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardActivity extends PActivity {
    private User currentUser;
    private HashMap<Folder, FolderAccess> userFolders;
    private RecyclerView folderRecyclerView;
    private FolderAdapter folderAdapter;
    public ImageView optionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        // Define vars
        currentUser = PosteApplication.getCurrentUser();
        optionsView = findViewById(R.id.Optionsbtn);
        folderRecyclerView = findViewById(R.id.folder_recycler_view);
        Button addButton = findViewById(R.id.dashboard_add_folder_btn);
        try {
            userFolders = API.getFoldersForUserId(currentUser.getId());
        } catch (APIException e) {
            throw new RuntimeException(e);
        }

        // Options button
        optionsView.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, OptionsActivity.class);
            startActivity(intent);
        });


        // Add folder button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the create item dialog
                showCreateItemDialog();
            }
        });

        // Fill folder view (Recycler View)
        folderRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        folderAdapter = new FolderAdapter(
                new FolderAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, Folder model) {
                        Intent intent = new Intent(DashboardActivity.this, FolderViewActivity.class );
                        intent.putExtra("folderId", model.getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, Folder model) {
                        Toast.makeText(DashboardActivity.this, getString(R.string.share_folder), Toast.LENGTH_LONG).show();
                    }
                },
                new ArrayList<>(userFolders.keySet())
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
        Button buttonCreateItem = dialogView.findViewById(R.id.buttonCreateItem);

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


                    // Handle post creation logic
                    // For simplicity, let's just display a toast message with the post details
                    Toast.makeText(DashboardActivity.this, "Post created:\nName: " + itemName + "\nLink: " + itemLink, Toast.LENGTH_LONG).show();

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
        Button buttonCreateItem = dialogView.findViewById(R.id.buttonCreateItem);

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
                    try {
                        int newFolderId = API.addFolder(itemName, currentUser.getId());
                        API.addUserToFolder(currentUser.getId(), newFolderId, FolderAccess.OWNER);

                        userFolders = API.getFoldersForUserId(currentUser.getId());
                        folderAdapter.notifyItemInserted(userFolders.size() - 1);
                    } catch (APIException e) {
                        throw new RuntimeException(e);
                    }

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
                try {
                    // Remove all access
                    HashMap<Integer, FolderAccess> folderUsers = API.getAccessForFolderId(folder.getId());
                    folderUsers.forEach((userId, folderAccess) -> {
                        try {
                            API.removeUserFromFolder(folder.getId(), userId);
                        } catch (APIException e) { }
                    });

                    API.deleteFolder(folder.getId());
                } catch (APIException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
        return true;
    }

}

