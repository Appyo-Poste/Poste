package com.example.poste.adapters;

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
import com.example.poste.models.Folder;

import java.util.List;

/**
 * The FolderAdapter class is a custom RecyclerView adapter used to display a list of folders
 * It binds the folder data to the corresponding views and handles user interactions
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private final ClickListener clickListener;
    private List<com.example.poste.models.Folder> localDataSet;
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
            imageView = view.findViewById(R.id.folder_image_view);
            textView = view.findViewById(R.id.folder_text_view);
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
            // Add context menu items (e.g., edit, share, delete) for each folder item
            menu.add(Menu.NONE, R.id.ctx_menu_edit_folder, Menu.NONE, R.string.edit);
            menu.add(Menu.NONE, R.id.ctx_menu_share_folder, Menu.NONE, R.string.share);
            menu.add(Menu.NONE, R.id.ctx_menu_delete_folder, Menu.NONE, R.string.delete);
        }
    }

    /**
     * Interface to define click listeners for folder items.
     */
    public interface ClickListener {
        void onItemClick(int position, com.example.poste.models.Folder model);
        void onItemLongClick(int position, Folder model);
    }

    public FolderAdapter(ClickListener clickListener, List<com.example.poste.models.Folder> folderList) {
        this.clickListener = clickListener;
        localDataSet = folderList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.folder_view_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        try {
            // Set the folder name
            viewHolder.getTextView().setText(localDataSet.get(position).getTitle());
        } catch (Exception e) {
            Log.e("FolderAdapter", e.getLocalizedMessage());
        }
    }

    public List<com.example.poste.models.Folder> getLocalDataSet(){
        return localDataSet;
    }

    public com.example.poste.models.Folder getLocalDataSetItem(){
        return localDataSet.get(position);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}