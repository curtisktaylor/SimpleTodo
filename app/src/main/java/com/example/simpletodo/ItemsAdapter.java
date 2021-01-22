package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//display data from model into row inside recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener OnLongClickListener;
    OnClickListener OnClickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener LongClickListener, OnClickListener ClickListener) {
        this.items = items;
        this.OnLongClickListener = LongClickListener;
        this.OnClickListener = ClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use a layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //Wrap it inside a view holder and return
        return new ViewHolder(todoView);
    }

    //binds data to a viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab item at position
        String item = items.get(position);
        //Bind to specified viewholder
        holder.bind(item);
    }

    //returns how many items are in list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //Update view inside viewholder with this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Notify listener what item was long pressed
                    OnLongClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
