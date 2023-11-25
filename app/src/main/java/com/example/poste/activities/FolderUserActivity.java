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
import android.widget.Toast;

import com.example.poste.R;
import com.example.poste.adapters.UserAdapter;
import com.example.poste.http.MyApiService;
import com.example.poste.http.RetrofitClient;
import com.example.poste.http.UpdateFolderPermissionsRequest;
import com.example.poste.models.Folder;
import com.example.poste.models.FolderAccess;
import com.example.poste.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                // get the email associated with this recycler view item
                String email = sharedUsers.get(position);
                MyApiService apiService = RetrofitClient.getRetrofitInstance().create(MyApiService.class);
                Call<ResponseBody> call = apiService.updateFolderPermissions(
                        User.getUser().getTokenHeaderString(),
                        new UpdateFolderPermissionsRequest(folderID, email, FolderAccess.NONE));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(FolderUserActivity.this,"Unshared folder with " + email, Toast.LENGTH_SHORT).show();
                            sharedUsers.remove(position);
                            recyclerView.getAdapter().notifyItemRemoved(position);
                            recyclerView.getAdapter().notifyItemRangeChanged(position, sharedUsers.size());
                        } else {
                            Toast.makeText(FolderUserActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(FolderUserActivity.this, "Unable to communicate with server", Toast.LENGTH_SHORT).show();
                    }
                });

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
