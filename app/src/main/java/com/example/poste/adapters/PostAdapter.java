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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ClickListener clickListener;
    private List<Post> localDataSet;
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
            //menuInfo is null
            menu.add(Menu.NONE, R.id.ctx_menu_edit_post,
                    Menu.NONE, R.string.edit);
            menu.add(Menu.NONE, R.id.ctx_menu_delete_post,
                    Menu.NONE, R.string.delete);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, Post model);
        void onItemLongClick(int position, Post model);
    }

    /**
     * Initialize the dataset of the Adapter
     *
     */
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
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        try {
            // Set the post name
            viewHolder.getTextView().setText(localDataSet.get(position).getName());
        }catch(Exception e){
            Log.e("hello", e.getLocalizedMessage() );
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public List<Post> getLocalDataSet(){
        return localDataSet;
    }

    public Post getLocalDataSetItem(){
        return localDataSet.get(position);
    }
}