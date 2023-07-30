package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.adapters.FolderAdapter;
import com.example.poste.api.poste.API;
import com.example.poste.api.poste.exceptions.APIException;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.FolderAccess;
import com.example.poste.api.poste.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class EditPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    private String previousFolderId;
    private String previousItemId;
    private User currentUser;
    private HashMap<Folder, FolderAccess> userFolders;

    private FolderAdapter folderAdapter;
    public ImageView optionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        spinner = findViewById(R.id.edit_post_spinner);
        spinner.setOnItemSelectedListener(this);
        new AsyncGetFolders().execute();
        
        previousFolderId = getIntent().getStringExtra("folderId");
        previousItemId = getIntent().getStringExtra("postId");

        currentUser = PosteApplication.getCurrentUser();
        optionsView = findViewById(R.id.Optionsbtn);
        Button addButton = findViewById(R.id.dashboard_add_folder_btn);
        Button saveBtn = findViewById(R.id.edit_post_save_button);

        saveBtn.setOnClickListener(view -> {

        });

        // Add folder button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the create item dialog
                showCreateItemDialog();
            }
        });

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
                Toast.makeText(EditPostActivity.this, "Post created:\nName: " + itemName + "\nLink: " + itemLink, Toast.LENGTH_LONG).show();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncGetFolders extends AsyncTask<String, String, List<Folder>> {
        ProgressDialog pdLoading = new ProgressDialog(EditPostActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected List<Folder> doInBackground(String... params) {
            try {
                HashMap<Folder, FolderAccess> userFolders = API.getFoldersForUserId(PosteApplication.getCurrentUser().getId());
                return new ArrayList<>(userFolders.keySet());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Folder> folders) {

            //this method will be running on UI
            LinkedHashMap<String, String> folderHash = new LinkedHashMap<>();
            for (Folder item: folders
                 ) {
                folderHash.put(String.valueOf(item.getId()),item.getName());
            }
            pdLoading.dismiss();
            spinner.setAdapter(new HashMapAdapter(folderHash));
        }
    }

    public class HashMapAdapter extends BaseAdapter {
        private LinkedHashMap<String, String> mData;
        private String[] mKeys;
        public HashMapAdapter(LinkedHashMap<String, String> data){
            mData  = data;
            mKeys = mData.keySet().toArray(new String[data.size()]);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(mKeys[position]);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            String key = mKeys[pos];
            String value = getItem(pos).toString();

            final View result;

            if (convertView == null) {
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_post_item, parent, false);
            } else {
                result = convertView;
            }

            // TODO replace findViewById by ViewHolder
            ((TextView) result.findViewById(R.id.edit_post_folder_name)).setText(value);
            ((TextView) result.findViewById(R.id.edit_post_folder_id)).setText(key);

            return result;
            //do your view stuff here
        }
    }

}