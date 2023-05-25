package com.example.poste;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PosteApplication;
import com.example.poste.database.AppRepository;
import com.example.poste.database.entity.Folder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class EditPost extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    private String previousFolderId;
    private String previousItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        spinner = findViewById(R.id.edit_post_spinner);
        spinner.setOnItemSelectedListener(this);
        new AsyncGetFolders().execute();
        
        previousFolderId = getIntent().getStringExtra("folderId");
        previousItemId = getIntent().getStringExtra("postId");

        Button saveBtn = findViewById(R.id.edit_post_save_button);

        saveBtn.setOnClickListener(view -> {

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncGetFolders extends AsyncTask<String, String, List<Folder>> {
        ProgressDialog pdLoading = new ProgressDialog(EditPost.this);

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
                AppRepository appRepository = new AppRepository(PosteApplication.getApp());
                String username = appRepository.getFirstUsername();
                return appRepository.getFolders(username);
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
                folderHash.put(String.valueOf(item.folder_id),item.folder_name);
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