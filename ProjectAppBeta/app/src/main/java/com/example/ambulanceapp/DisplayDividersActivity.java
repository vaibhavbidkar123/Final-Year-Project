package com.example.ambulanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

public class DisplayDividersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MapPointAdapter mapPointAdapter;
    private MapPointRepository mapPointRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_dividers);

        mapPointRepository = new MapPointRepository(userDatabase.getInstance(getApplicationContext()).mapPointDao()); // Initialize UserRepository

        recyclerView = findViewById(R.id.recyclerView);
        mapPointAdapter = new MapPointAdapter();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mapPointAdapter);

        // Set the delete click listener
        mapPointAdapter.setOnDeleteClickListener(position -> deleteMapPoint(position));

        // Load users from the database and update the RecyclerView
        loadMapPoints();
    }

    private void deleteMapPoint(int position) {
        MapPoints mapPointsToDelete = mapPointAdapter.getMapPoints().get(position);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mapPointRepository.deleteMapPoint(mapPointsToDelete);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                // Refresh the RecyclerView after deletion
                loadMapPoints();
            }
        }.execute();
    }

    private void loadMapPoints() {
        // Use AsyncTask to perform database operation in the background
        new AsyncTask<Void, Void, List<MapPoints>>() {
            @Override
            protected List<MapPoints> doInBackground(Void... voids) {
                // Retrieve the list of users from the database
                return mapPointRepository.getAllMapPoints();
            }

            @Override
            protected void onPostExecute(List<MapPoints> mapPoints) {
                super.onPostExecute(mapPoints);

                // Update the RecyclerView with the list of users
                mapPointAdapter.setMapPoints(mapPoints);
            }
        }.execute();
    }
}