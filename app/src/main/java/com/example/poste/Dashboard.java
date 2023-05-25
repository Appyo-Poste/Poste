package com.example.poste;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.FolderAdapter;
import com.example.PosteApplication;
import com.example.poste.database.AppRepository;
import com.example.poste.database.entity.Folder;
import com.example.poste.database.entity.UserFolder;

import java.util.List;

public class Dashboard extends AppCompatActivity {
    public Button buttonLinkAccount;
    public Button buttonShare;
    public ImageView Options;
    public String UserID;
    private RecyclerView mFolderRecyclerView;
    private FolderAdapter mFolderAdapter;
    private AppRepository appRepository;
    private List<Folder> folderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        appRepository = new AppRepository(PosteApplication.getApp());
        folderList = appRepository.getFolders(appRepository.getFirstUsername());

        Options = findViewById(R.id.Optionsbtn);
        Button buttonAddFolder = findViewById(R.id.dashboard_add_folder_btn);

        //Opens The OptionsPage
        Options.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, OptionsActivity.class);
            startActivity(intent);
        });

        buttonAddFolder.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Folder");

// Set up the input
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("Create", (dialog, which) -> {
                Folder folder = new Folder();
                folder.folder_name = input.getText().toString();
                folder.shared = false;
                long id = appRepository.insertFolder(folder);
                UserFolder userFolder = new UserFolder();
                userFolder.folder_id = String.valueOf(id);
                userFolder.email = appRepository.getFirstUsername();
                appRepository.insertUserFolder(userFolder);
                folderList.add(folder);
                mFolderAdapter.notifyItemInserted(folderList.size() - 1);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();

        });

        // BEGIN_INCLUDE(initializeRecyclerView)
        mFolderRecyclerView = findViewById(R.id.folder_recycler_view);
        mFolderRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mFolderAdapter = new FolderAdapter(
                new FolderAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, Folder model) {
                        Intent intent = new Intent(Dashboard.this, FolderView.class );
                        intent.putExtra("folderId", String.valueOf(model.folder_id));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, Folder model) {
                        Toast.makeText(Dashboard.this, "Share this folder", Toast.LENGTH_LONG).show();
                    }
                },
                folderList
                );
        mFolderRecyclerView.setAdapter(mFolderAdapter);
        registerForContextMenu(mFolderRecyclerView);

        // END_INCLUDE(initializeRecyclerView)
    }

    // menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Folder folder = mFolderAdapter.getLocalDataSetItem();
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_folder:
                intent = new Intent(Dashboard.this, EditFolder.class );
                intent.putExtra("folderId", folder.folder_id);
                intent.putExtra("folderName", folder.folder_name);
                intent.putExtra("folderShared", folder.shared);
                startActivity(intent);
                finish();
                break;
            case R.id.ctx_menu_share_folder:
                intent = new Intent(Dashboard.this, Shared_Folder.class);
                intent.putExtra("folderId", folder.folder_id);
                intent.putExtra("folderName", folder.folder_name);
                startActivity(intent);
                finish();
                break;
            case R.id.ctx_menu_delete_folder:
                appRepository.deleteFolder(folder);
        }
        return true;
    }

}