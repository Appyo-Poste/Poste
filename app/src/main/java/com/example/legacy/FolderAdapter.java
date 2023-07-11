package com.example.legacy;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.R;
import com.example.legacy.poste.database.AppRepository;
import com.example.legacy.poste.database.entity.Folder;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private final ClickListener clickListener;
    private List<Folder> localDataSet;
    public int position;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
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
            return false;/*
            if (position >= 0) {
                clickListener.onItemLongClick(position, localDataSet.get(position));
                return true;
            }
            return false;*/
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menuInfo is null
            menu.add(Menu.NONE, R.id.ctx_menu_edit_folder,
                    Menu.NONE, R.string.edit);
            menu.add(Menu.NONE, R.id.ctx_menu_share_folder,
                    Menu.NONE, R.string.share);
            menu.add(Menu.NONE, R.id.ctx_menu_delete_folder,
                    Menu.NONE, R.string.delete);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, Folder model);
        void onItemLongClick(int position, Folder model);
    }

    /**
     * Initialize the dataset of the Adapter
     *
     */
    public FolderAdapter(ClickListener clickListener, List<Folder> folderList) {
        this.clickListener = clickListener;
        AppRepository appRepository = new AppRepository(PosteApplication.getApp());
        String username = appRepository.getFirstUsername();
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

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        try {
            viewHolder.getTextView().setText(localDataSet.get(position).folder_name);
            //viewHolder.imageView.setImageResource(R.drawable.folder_icon);
        }catch(Exception e){
            Log.e("hello", e.getLocalizedMessage() );
        }
    }

    public List<Folder> getLocalDataSet(){
        return localDataSet;
    }

    public Folder getLocalDataSetItem(){
        return localDataSet.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}