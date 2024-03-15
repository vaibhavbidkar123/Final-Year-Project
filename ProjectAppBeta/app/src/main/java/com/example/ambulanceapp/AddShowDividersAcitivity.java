package com.example.ambulanceapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddShowDividersAcitivity extends AppCompatActivity {


    private Button addDivider,viewDivider;
    private MapPointRepository mapPointRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_show_dividers_acitivity);

        addDivider=findViewById(R.id.AddDividerButton);
        viewDivider=findViewById(R.id.ViewDividerButton);


        //after clicking on register button , register form will open
        addDivider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddShowDividersAcitivity.this, ManageDividersActivity.class);
                startActivity(intent);
            }
        });


        //after clicking on login button login form will open
        viewDivider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddShowDividersAcitivity.this, ViewDividersOnMapsActivity.class);
//                startActivity(intent);
                new AddShowDividersAcitivity.GetDirectionsTask().execute();
            }
        });
    }


    private class GetDirectionsTask extends AsyncTask<Void, Void, List<MapPoints>> {

        @Override
        protected List<MapPoints> doInBackground(Void... voids) {
            // List to store LatLng points retrieved from the database

            // Retrieve data from the Room database
            mapPointRepository = new MapPointRepository(userDatabase.getInstance(getApplicationContext()).mapPointDao());
            List<MapPoints> dividersList = mapPointRepository.getAllMapPoints();

//            for (int i = 0; i < dividersList.size(); i++) {
//                        MapPoints latLng = dividersList.get(i);
//                        Log.d("Bug", "Divider " + i + ": " + latLng.latitude + ", " + latLng.longitude);
//                    }



            return dividersList;
        }

        @Override
        protected void onPostExecute(List<MapPoints> dividersList) {
            // Handle the retrieved data in the UI thread after doInBackground() finishes
            // This method is invoked on the UI thread, so you can safely update UI components here
            // For example, you can display the retrieved points on a map or perform other UI operations
            Intent intent = new Intent(AddShowDividersAcitivity.this, ViewDividersOnMapsActivity.class);
            intent.putExtra("dividersList", new ArrayList<>(dividersList));
            startActivity(intent);

        }
    }

}