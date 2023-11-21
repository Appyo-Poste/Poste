package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.poste.R;
import com.example.poste.adapters.UserAdapter;
import com.example.poste.models.Folder;
import com.example.poste.models.User;

import java.util.List;

public class FolderUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_user);
        Folder folder = User.getUser().getSelectedFolder();
        List<String> sharedUsers = folder.getSharedUsers();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);

        UserAdapter.ClickListener cl = new UserAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, String user) {
                Log.d("UserDebug", "Clicked: User " + (position + 1) + ": " + user);
            }

            @Override
            public void onItemLongClick(int position, String model) {
                Log.d("UserDebug", "Long Clicked: User " + (position + 1) + ": " + model);
            }
        };


        UserAdapter adapter = new UserAdapter(cl, sharedUsers);

        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
