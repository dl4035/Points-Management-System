package com.example.fetch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Item {
    int listId;
    int id;
    String name;
}

class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        if (o1.listId == o2.listId) {
            return o1.name.compareTo(o2.name);
        }
        return Integer.compare(o1.listId, o2.listId);
    }
}

class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> items;

    // Constants for view types
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        // Implement logic to check if the current position is a header
        // Here, checking if it's the first item of each listId group
        if (position == 0 || items.get(position).listId != items.get(position - 1).listId) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).headerTitle.setText("List ID: " + items.get(position).listId);
        } else {
            ((ItemViewHolder) holder).itemName.setText(items.get(position).name);
            ((ItemViewHolder) holder).itemDetails.setText("ID: " + items.get(position).id);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTitle = itemView.findViewById(R.id.headerTitle);
        }
    }

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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://fetch-hiring.s3.amazonaws.com/hiring.json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    Type type = new TypeToken<List<Item>>() {
                    }.getType();
                    items = new Gson().fromJson(jsonData, type);
                    // Filter and sort
                    items.removeIf(item -> item.name == null || item.name.trim().isEmpty());
                    items.sort(new ItemComparator());

                    runOnUiThread(() -> recyclerView.setAdapter(new ItemAdapter(items)));
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
