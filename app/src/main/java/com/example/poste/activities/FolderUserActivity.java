package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.poste.R;
import com.example.poste.adapters.UserAdapter;
import com.example.poste.models.Folder;
import com.example.poste.models.User;

import java.util.List;

public class FolderUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_folder_user);


        String folderID = getIntent().getStringExtra("folderId");
        String folderName = getIntent().getStringExtra("folderName");

        if (folderID == null || folderName == null) {
            Log.d("FolderUserDebug", "Folder ID or folder name is null");
            finish();
        }


        Folder folder = User.getUser().getFolder(folderID);
        List<String> sharedUsers = folder.getSharedUsers();
        TextView folderNameText = findViewById(R.id.folderNameText);
        folderNameText.setText(folderName);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);

        UserAdapter.ClickListener cl = new UserAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, String user) {
                Log.d("UserDebug", "Clicked: User " + (position + 1) + ": " + user);
                // unshare folder with user

            }

            @Override
            public void onItemLongClick(int position, String model) {
                Log.d("UserDebug", "Long Clicked: User " + (position + 1) + ": " + model);
            }
        };



        registerForContextMenu(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        UserAdapter adapter = new UserAdapter(cl, sharedUsers);
        recyclerView.setAdapter(adapter);

    }
}
