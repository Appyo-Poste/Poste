package com.example.legacy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.poste.R;
import com.example.legacy.poste.database.AppRepository;
import com.example.legacy.poste.database.entity.PosteItem;

import java.io.InputStream;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<PosteItem> localDataSet;
    public int position;
    private ClickListener clickListener;

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
            imageView = (ImageView) view.findViewById(R.id.recycler_image_view);
            textView = (TextView) view.findViewById(R.id.recyclerTextView);
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
        }
    }

    public interface ClickListener {
        void onItemClick(int position, PosteItem model);
        void onItemLongClick(int position, PosteItem model);
    }

    /**
     * Initialize the dataset of the Adapter
     *
     */
    public PostAdapter(String folderId, ClickListener clickListener) {
        AppRepository appRepository = new AppRepository(PosteApplication.getApp());
        localDataSet = appRepository.getPostByFolder(folderId);
        this.clickListener = clickListener;
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
            viewHolder.getTextView().setText(localDataSet.get(position).text);
            ImageView imageView = viewHolder.getImageView();
            if(localDataSet.get(position).media_url != null)
                new DownloadImageFromInternet(imageView).execute(localDataSet.get(position).media_url);
            else
                imageView.setVisibility(View.GONE);
        }catch(Exception e){
            Log.e("hello", e.getLocalizedMessage() );
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    public List<PosteItem> getLocalDataSet(){
        return localDataSet;
    }

    public PosteItem getLocalDataSetItem(){
        return localDataSet.get(position);
    }
}