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

/**
 * MainActivity is responsible for fetching, displaying, and grouping data.
 */
public class MainActivity extends AppCompatActivity {

    // UI components and data storage structures
    RecyclerView recyclerView;
    List<Item> items = new ArrayList<>();
    HashMap<Integer, List<Item>> groupedItems = new HashMap<>();
    Button btnGroup1, btnGroup2, btnGroup3, btnGroup4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize buttons
        btnGroup1 = findViewById(R.id.btnGroup1);
        btnGroup2 = findViewById(R.id.btnGroup2);
        btnGroup3 = findViewById(R.id.btnGroup3);
        btnGroup4 = findViewById(R.id.btnGroup4);

        // Set content descriptions for accessibility
        btnGroup1.setContentDescription("Show items for Group 1");
        btnGroup2.setContentDescription("Show items for Group 2");
        btnGroup3.setContentDescription("Show items for Group 3");
        btnGroup4.setContentDescription("Show items for Group 4");

        // Set the click listeners for the buttons
        setGroupButtonClickListener(btnGroup1, 1);
        setGroupButtonClickListener(btnGroup2, 2);
        setGroupButtonClickListener(btnGroup3, 3);
        setGroupButtonClickListener(btnGroup4, 4);

        // Fetch and process data
        fetchData();
    }

    /**
     * Set click listeners for group buttons.
     *
     * @param button Button to which the click listener will be attached.
     * @param groupId Group ID to be used for displaying data.
     */
    private void setGroupButtonClickListener(Button button, int groupId) {
        button.setOnClickListener(view -> {
            displayGroup(groupId);

            // Send an accessibility event to announce the change with Talkback
            recyclerView.announceForAccessibility("Displaying items for Group " + groupId);
        });
    }

    /**
     * Fetches data from the provided URL, processes the data,
     * and updates the UI.
     */
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
                    Type type = new TypeToken<List<Item>>() {}.getType();
                    items = new Gson().fromJson(jsonData, type);

                    // Filter and sort
                    items.removeIf(item -> item.name == null || item.name.trim().isEmpty());
                    items.sort(new ItemComparator());

                    // Group the items
                    for (Item item : items) {
                        if (!groupedItems.containsKey(item.listId)) {
                            groupedItems.put(item.listId, new ArrayList<>());
                        }
                        groupedItems.get(item.listId).add(item);
                    }

                    runOnUiThread(() -> {
                        recyclerView.setAdapter(new ItemAdapter(items));

                        // Announce for accessibility when new data is loaded
                        recyclerView.announceForAccessibility("New items loaded.");
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    /**
     * Displays items for a particular group in the RecyclerView.
     *
     * @param groupId Group ID for which the data will be displayed.
     */
    private void displayGroup(int groupId) {
        List<Item> group = groupedItems.get(groupId);
        if (group != null) {
            recyclerView.setAdapter(new ItemAdapter(group));
        } else {
            Toast.makeText(this, "No items for Group " + groupId, Toast.LENGTH_SHORT).show();
        }
    }
}