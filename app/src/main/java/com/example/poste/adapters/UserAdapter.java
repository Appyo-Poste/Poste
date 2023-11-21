package com.example.poste.adapters;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poste.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final ClickListener clickListener;
    private List<String> localDataSet;

    public int position;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

        private final TextView textView;

        private final Button button;

        public ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.user_button_view);
            textView = view.findViewById(R.id.user_text_view);
            button.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        public Button getButton() {
            return button;
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
            return;

        }
    }

    public interface ClickListener {
        void onItemClick(int position, String model);
        void onItemLongClick(int position, String model);
    }

    public UserAdapter(ClickListener clickListener, List<String> dataSet) {
        this.clickListener = clickListener;
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        try {
            viewHolder.getTextView().setText(localDataSet.get(position));
        } catch (Exception e) {
            Log.e("UserAdapter", "Error binding view holder: " + e.getMessage());
        }
    }

    public List<String> getLocalDataSet(){
        return localDataSet;
    }

    public String getLocalDataSetItem() {
        return localDataSet.get(position);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
