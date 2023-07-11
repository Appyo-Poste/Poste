package com.example.poste.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
        Button buttonAddFolder = findViewById(R.id.dashboard_add_folder_btn);
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
        buttonAddFolder.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.new_folder));

            // Set up the input
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Create button
            builder.setPositiveButton(getString(R.string.create), (dialog, which) -> {
                try {
                    int newFolderId = API.addFolder(input.getText().toString(), currentUser.getId());
                    API.addUserToFolder(currentUser.getId(), newFolderId, FolderAccess.OWNER);

                    userFolders = API.getFoldersForUserId(currentUser.getId());
                    folderAdapter.notifyItemInserted(userFolders.size() - 1);
                } catch (APIException e) {
                    throw new RuntimeException(e);
                }
            });

            // Cancel button
            builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());

            builder.show();
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