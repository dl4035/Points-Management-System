package com.example.fetch;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.fetch.Item;
import com.example.fetch.ItemComparator;
import com.example.fetch.ItemAdapter;

public class MainActivity extends AppCompatActivity {

    // Views and data containers
    RecyclerView recyclerView;
    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Start fetching data
        fetchData();
    }

    /**
     * Fetches data from the provided URL and populates the RecyclerView
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

                    // Filter items with empty or null names and sort them
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
