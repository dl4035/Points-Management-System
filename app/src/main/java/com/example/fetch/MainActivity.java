package com.example.fetch;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Item> items = new ArrayList<>();
    HashMap<Integer, List<Item>> groupedItems = new HashMap<>();

    Button btnGroup1, btnGroup2, btnGroup3, btnGroup4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnGroup1 = findViewById(R.id.btnGroup1);
        btnGroup2 = findViewById(R.id.btnGroup2);
        btnGroup3 = findViewById(R.id.btnGroup3);
        btnGroup4 = findViewById(R.id.btnGroup4);

        btnGroup1.setOnClickListener(view -> displayGroup(1));
        btnGroup2.setOnClickListener(view -> displayGroup(2));
        btnGroup3.setOnClickListener(view -> displayGroup(3));
        btnGroup4.setOnClickListener(view -> displayGroup(4));

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

                    for (Item item : items) {
                        if (!groupedItems.containsKey(item.listId)) {
                            groupedItems.put(item.listId, new ArrayList<>());
                        }
                        groupedItems.get(item.listId).add(item);
                    }

                    runOnUiThread(() -> recyclerView.setAdapter(new ItemAdapter(items)));
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void displayGroup(int groupId) {
        List<Item> group = groupedItems.get(groupId);
        if (group != null) {
            recyclerView.setAdapter(new ItemAdapter(group));
        } else {
            Toast.makeText(this, "No items for Group " + groupId, Toast.LENGTH_SHORT).show();
        }
    }
}
