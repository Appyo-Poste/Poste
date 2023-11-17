package com.example.poste.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poste.R;
import com.example.poste.adapters.PostAdapter;
import com.example.poste.callbacks.PostDeletionCallback;
import com.example.poste.models.Folder;
import com.example.poste.models.Post;
import com.example.poste.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Search activity for application
 * Allows user to search for posts across folders
 */
public class SearchActivity extends AppCompatActivity {

    /**
     * Fields users can search by
     */
    private enum SEARCH_FIELD {
        LINK,
        TITLE,
        DESCRIPTION,
        TAG
    }

    private PostAdapter postAdapter;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        // prepare variables
        searchBtn = findViewById(R.id.search_go_button);
        EditText searchByEditText = findViewById(R.id.editTextSearchBy);
        Spinner chooseSearchField = findViewById(R.id.chooseFieldSpinner);
        RecyclerView showPosts = findViewById(R.id.posts_recycler_view);
        TextView emptyText = findViewById(R.id.searchViewEmptyText);
        emptyText.setVisibility(View.GONE);

        // setup search field spinner
        List<String> searchFields = new ArrayList<>();
        searchFields.addAll(Arrays.asList(getResources().getStringArray(R.array.spinner_items)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchFields);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSearchField.setAdapter(adapter);

        // when Go! button is clicked
        searchBtn.setOnClickListener(v -> {
            String searchField = (String) chooseSearchField.getSelectedItem();
            String searchBy = searchByEditText.getText().toString();

            if (searchField.equals(searchFields.get(0))) {
                Log.d("SearchActivity", "Default selected");
                Toast.makeText(this, "Select a field to search post!", Toast.LENGTH_SHORT).show();
            } else {
                if (searchBy.isBlank()){
                    Toast.makeText(this, "Search field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("SearchActivity", Integer.toString(chooseSearchField.getSelectedItemPosition()));
                    Log.d("SearchActivity", searchBy);
                    List<Post> searchResultList = searchResult(SEARCH_FIELD.values()[chooseSearchField.getSelectedItemPosition() - 1], searchBy);
                    // Fill post view (Recycler View)
                    if (searchResultList.size() == 0){
                        emptyText.setVisibility(View.VISIBLE);
                        showPosts.setVisibility(View.GONE);
                    } else {
                        emptyText.setVisibility(View.GONE);
                        showPosts.setVisibility(View.VISIBLE);
                        showPosts.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                        postAdapter = new PostAdapter(
                                new PostAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, Post post) {
                                        User.getUser().setSelectedPost(post);
                                        try {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(User.getUser().getSelectedPost().getUrl()));
                                            startActivity(browserIntent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(SearchActivity.this, R.string.internal_error, Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onItemLongClick(int position, Post post) {
                                        User.getUser().setSelectedPost(post);
                                    }
                                },
                                searchResult(SEARCH_FIELD.values()[chooseSearchField.getSelectedItemPosition() - 1], searchBy)
                        );
                        showPosts.setAdapter(postAdapter);
                    }
                }
            }
        });
    }

    /**
     * helper function to generate a list of posts the meet query
     * @param field to search by
     * @param queryString criteria
     * @return List of Posts that meet criteria
     */
    private List<Post> searchResult(SEARCH_FIELD field, String queryString){
        List<Post> searchResultList = new ArrayList<>();
        for (Folder f:
             User.getUser().getFolders()) {
            for (Post p:
                f.getPosts()){
                switch (field){
                    case LINK:
                        if (p.getUrl().contains(queryString)){
                            searchResultList.add(p);
                        }
                        break;
                    case TITLE:
                        if (p.getTitle().contains(queryString)){
                            searchResultList.add(p);
                        }
                        break;
                    case DESCRIPTION:
                        if (p.getDescription().contains(queryString)){
                            searchResultList.add(p);
                        }
                        break;
                    //TODO: Add search for tags. Waiting for implementation
                }
            }
        }
        return searchResultList;
    }

    /**
     * On resume run the search again to update the list
     */
    @Override
    protected void onResume() {
        super.onResume();
        searchBtn.callOnClick();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Post post = postAdapter.getLocalDataSetItem();
        switch (item.getItemId()) {
            case R.id.ctx_menu_edit_post:
                User.getUser().setSelectedPost(post);
                if (User.getUser().getSelectedPost() != null) {
                    Intent intent = new Intent(SearchActivity.this, EditPostActivity.class);
                    intent.putExtra("postID", post.getId());
                    startActivity(intent);
                }
                break;
            case R.id.ctx_menu_delete_post:
                User.getUser().setSelectedPost(post);
                if (User.getUser().getSelectedPost() != null) {
                    PostDeletionCallback postDeletionCallback = new PostDeletionCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(SearchActivity.this, R.string.post_deleted, Toast.LENGTH_LONG).show();
                            recreate();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(SearchActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            recreate();
                        }
                    };
                    User.getUser().deletePostFromServer(User.getUser().getSelectedPost(), postDeletionCallback);
                }
                break;
        }
        return true;
    }
}