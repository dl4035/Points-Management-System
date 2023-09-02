package com.example.fetch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This adapter is responsible for binding the data of items to the RecyclerView.
 * It manages both header views (to display the listId) and item views (to display individual item details).
 */
public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items;

    // Constants for view types to differentiate between headers and items
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    /**
     * Constructor for the ItemAdapter.
     *
     * @param items List of items to be displayed in the RecyclerView.
     */
    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    /**
     * Determines the type of view to be displayed at a particular position.
     *
     * @param position Position in the items list.
     * @return Type of view. It can be TYPE_HEADER or TYPE_ITEM.
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 || items.get(position).listId != items.get(position - 1).listId) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder of the specified type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the specified view type.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    /**
     * Called to bind the data with the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated to represent the contents of the item.
     * @param position The position within the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).headerTitle.setText("List ID: " + items.get(position).listId);
        } else {
            ((ItemViewHolder) holder).itemName.setText(items.get(position).name);
            ((ItemViewHolder) holder).itemDetails.setText("ID: " + items.get(position).id);
        }
    }

    /**
     * Returns the total number of items in the dataset.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * ViewHolder class for header items.
     */
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.headerTitle);
        }
    }

    /**
     * ViewHolder class for individual item details.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDetails;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDetails = itemView.findViewById(R.id.itemDetails);
        }
    }
}