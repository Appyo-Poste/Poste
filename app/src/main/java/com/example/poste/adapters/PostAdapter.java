package com.example.poste.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.PosteApplication;
import com.example.poste.R;
import com.example.poste.api.poste.models.Folder;
import com.example.poste.api.poste.models.Post;

import java.io.InputStream;
import java.util.List;

/**
 * The PostAdapter class is a custom RecyclerView adapter used to display a list of posts
 * It binds the folder data to the corresponding views and handles user interactions
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ClickListener clickListener;
    private List<Post> localDataSet;
    public int position;

    /**
     * Custom ViewHolder class that holds the views of a single item in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define variables for the view
            imageView = view.findViewById(R.id.recycler_image_view);
            textView = view.findViewById(R.id.recyclerTextView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            position = this.getAdapterPosition();
            if (position >= 0) {
                clickListener.onItemClick(position, localDataSet.get(position));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            position = this.getAdapterPosition();
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // Add context menu items (e.g., edit, delete) for each folder item
            menu.add(Menu.NONE, R.id.ctx_menu_edit_post, Menu.NONE, R.string.edit);
            menu.add(Menu.NONE, R.id.ctx_menu_delete_post, Menu.NONE, R.string.delete);
        }
    }

    /**
     * Interface to define click listeners for post items.
     */
    public interface ClickListener {
        void onItemClick(int position, Post model);
        void onItemLongClick(int position, Post model);
    }

    public PostAdapter(ClickListener clickListener, List<Post> postList) {
        this.clickListener = clickListener;
        localDataSet = postList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        try {
            // Set the post name
            viewHolder.getTextView().setText(localDataSet.get(position).getName());
        } catch (Exception e) {
            Log.e("FolderAdapter", e.getLocalizedMessage());
        }
    }

    public List<Post> getLocalDataSet(){
        return localDataSet;
    }

    public Post getLocalDataSetItem(){
        return localDataSet.get(position);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}