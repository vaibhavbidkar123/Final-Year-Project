package com.example.ambulanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ManageDividersActivity extends AppCompatActivity {

    private MapPointRepository mapPointRepository;
    private EditText editTextLatitude;
    private EditText editTextLongitude;

    private EditText editTextDividerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_dividers);

        userDatabase db = userDatabase.getInstance(getApplicationContext());
        mapPointRepository = new MapPointRepository(db.mapPointDao());

        editTextLatitude = findViewById(R.id.LatitudeEditText);
        editTextLongitude = findViewById(R.id.LongitudeEditText);
        editTextDividerName =findViewById(R.id.DividerNameEditText);


        Button registerButton = findViewById(R.id.AddButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String latitude = editTextLatitude.getText().toString().trim();
                String longitude = editTextLongitude.getText().toString().trim();
                String dividerName = editTextDividerName.getText().toString().trim();

                // Check if name and password are not empty
//                if (name.isEmpty() || password.isEmpty() || name.equalsIgnoreCase("admin")) {
//                    Toast.makeText(RegisterActivity.this, "Invalid Name/Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Create a new user
                MapPoints mapPoints = new MapPoints();
                mapPoints.longitude = Double.parseDouble(longitude);
                mapPoints.latitude = Double.parseDouble(latitude);
                mapPoints.title = dividerName;


                // Use Executor to perform database operation in the background
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    // Register the user
                    mapPointRepository.add(mapPoints);

                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        // Show a success message
                        Toast.makeText(ManageDividersActivity.this, "Added a New Divider", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ManageDividersActivity.this, DisplayDividersActivity.class);
                        startActivity(intent);
                    });
                });
            }
        });
    }
}