package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HikeListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHikes;
    private HikeAdapter hikeAdapter;
    private List<Hike> hikeList;
    private DatabaseHelper databaseHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("All Hikes");
        }

        databaseHelper = new DatabaseHelper(this);

        recyclerViewHikes = findViewById(R.id.recyclerViewHikes);
        recyclerViewHikes.setLayoutManager(new LinearLayoutManager(this));

        hikeList = new ArrayList<>();
        hikeAdapter = new HikeAdapter(this, hikeList); // Pass context here
        recyclerViewHikes.setAdapter(hikeAdapter);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHikes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHikes(newText);
                return true;
            }
        });

        FloatingActionButton fabAddHike = findViewById(R.id.fabAddHike);
        fabAddHike.setOnClickListener(v -> {
            Intent intent = new Intent(HikeListActivity.this, AddHikeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHikesFromDatabase();
    }

    private void loadHikesFromDatabase() {
        // If search view is not empty, maintain the search results
        if (searchView != null && !searchView.getQuery().toString().isEmpty()) {
             searchHikes(searchView.getQuery().toString());
        } else {
            List<Hike> updatedHikeList = databaseHelper.getAllHikes();
            hikeAdapter.updateData(updatedHikeList);
        }
    }

    private void searchHikes(String query) {
        List<Hike> searchResults;
        if (query.isEmpty()) {
            searchResults = databaseHelper.getAllHikes();
        } else {
            searchResults = databaseHelper.searchHikes(query);
        }
        hikeAdapter.updateData(searchResults);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
