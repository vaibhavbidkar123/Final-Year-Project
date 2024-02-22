package com.example.ambulanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SetPathActivity extends AppCompatActivity {

    private AutoCompleteTextView source,destination;
    private Button path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_path);
        String[] languages={"Panjim ","Margao","Canacona","Navelim","Ponda","Mapusa","quepem ","Valpoi","Bambolim","Pednem","Savordem","porvorim"};
        source=(AutoCompleteTextView) findViewById(R.id.Source);
        destination=(AutoCompleteTextView) findViewById(R.id.Destination);
        path=findViewById((R.id.PathButton));
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,languages);

        source.setAdapter(adapter);
        source.setThreshold(1);

        destination.setAdapter(adapter);
        destination.setThreshold(1);


        path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate user inputs before proceeding
                String sourceText = source.getText().toString().trim();
                String destinationText = destination.getText().toString().trim();

                if (TextUtils.isEmpty(sourceText)) {
                    source.setError("Source cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(destinationText)) {
                    destination.setError("Destination cannot be empty");
                    return;
                }

                // Both source and destination are not empty, proceed with GetDirectionsTask
                new GetDirectionsTask().execute();
            }
        });



    }


    private String buildDirectionsUrl(
                                      List<LatLng> waypoints) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("maps.googleapis.com")
                .appendPath("maps")
                .appendPath("api")
                .appendPath("directions")
                .appendPath("json")
                .appendQueryParameter("origin", source.getText().toString())
                .appendQueryParameter("destination", destination.getText().toString());
        //https://maps.googleapis.com/maps/api/directions/json

        // Adding waypoints
        if (waypoints != null && waypoints.size() > 0) {
            StringBuilder waypointsBuilder = new StringBuilder();
            for (LatLng waypoint : waypoints) {
                waypointsBuilder.append(waypoint.latitude)
                        .append(",")
                        .append(waypoint.longitude)
                        .append("|");
            }
            waypointsBuilder.setLength(waypointsBuilder.length() - 1); // Remove the last '|'
            builder.appendQueryParameter("waypoints", waypointsBuilder.toString());
        }

        builder.appendQueryParameter("mode", "driving")
                .appendQueryParameter("key", "AIzaSyDhw_dv7xSxPQWCQtzg6SnfuIEHpHBB_vc"); // Replace with your actual API key

        return builder.build().toString();
    }


    private class GetDirectionsTask extends AsyncTask<Void, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(Void... voids) {
            List<LatLng> startLocationsList = new ArrayList<>();

            try {


                // Replace this with your actual waypoints if needed
                List<LatLng> waypoints = null;

                String directionsUrl = buildDirectionsUrl(
                        waypoints);

                // Make the HTTP request
                URL url = new URL(directionsUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");
                    String result = scanner.hasNext() ? scanner.next() : null;

                    if (result != null) {
                        try {
                            JSONObject jsonResponse = new JSONObject(result);

                            // Check if the "routes" array exists
                            if (jsonResponse.has("routes")) {
                                JSONArray routesArray = jsonResponse.getJSONArray("routes");

                                // Loop through each route
                                for (int i = 0; i < routesArray.length(); i++) {
                                    JSONObject route = routesArray.getJSONObject(i);

                                    // Check if the "legs" array exists
                                    if (route.has("legs")) {
                                        JSONArray legsArray = route.getJSONArray("legs");

                                        // Loop through each leg
                                        for (int j = 0; j < legsArray.length(); j++) {
                                            JSONObject leg = legsArray.getJSONObject(j);

                                            // Check if the "steps" array exists
                                            if (leg.has("steps")) {
                                                JSONArray stepsArray = leg.getJSONArray("steps");

                                                // Loop through each step
                                                for (int k = 0; k < stepsArray.length(); k++) {
                                                    JSONObject step = stepsArray.getJSONObject(k);

                                                    // Extract start location latitude and longitude
                                                    JSONObject startLocation = step.getJSONObject("start_location");
                                                    double startLat = startLocation.getDouble("lat");
                                                    double startLng = startLocation.getDouble("lng");

                                                    // Add the LatLng to the list
                                                    startLocationsList.add(new LatLng(startLat, startLng));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return startLocationsList;
        }

        @Override
        protected void onPostExecute(List<LatLng> startLocationsList) {
            // Log the list of start locations
            for (int i = 0; i < startLocationsList.size(); i++) {
                LatLng latLng = startLocationsList.get(i);
                Log.d("StartLocation", "Step " + i + ": " + latLng.latitude + ", " + latLng.longitude);
            }
            if(startLocationsList.contains(new LatLng(15.3757246, 73.9258352)) ||startLocationsList.contains(new LatLng(15.3733589, 74.0106969))  ||startLocationsList.contains(new LatLng(15.2982048, 73.97173699999999)) ){
                Toast.makeText(SetPathActivity.this, "Divider detected ", Toast.LENGTH_SHORT).show();
                Log.d("divider","divider detected");
            }else {
                Toast.makeText(SetPathActivity.this, "NO Divider detected ", Toast.LENGTH_SHORT).show();
                Log.d("divider","No divider detected");
            }
            Intent intent = new Intent(SetPathActivity.this, ShowRoute.class);
            intent.putParcelableArrayListExtra("myObjectList", new ArrayList<>(startLocationsList));
            startActivity(intent);

        }
    }

}