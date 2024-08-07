package com.example.ambulanceapp;



import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SetPathActivity extends AppCompatActivity {

    private AutoCompleteTextView source,destination;

    private ProgressDialog progressDialog;
    private Handler handler;
    private Button path;
    private MapPointRepository mapPointRepository;
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


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



    private class ConnectToRaspberryPiTask extends AsyncTask<Void, Void, String> {

        private static final String RASPBERRY_PI_IP = "192.168.227.83";
        //10.41.0.1
        //192.168.0.105

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://" + RASPBERRY_PI_IP + ":5000/check_connection");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // Read the response if needed
                    // For simplicity, we are not processing the response in this example
                } finally {
                    urlConnection.disconnect();
                }

                return "Connection successful!";
            } catch (IOException e) {
                e.printStackTrace();
                return "Connection failed!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Update UI or show a Toast based on the result
            // For simplicity, we are not updating the UI in this example
        }
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
                .appendQueryParameter("key", getString(R.string.google_maps_key)); // Replace with your actual API key

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


            // Update UI or show a Toast based on the result



            // Now, perform the database operations on the main thread
            new AsyncTask<Void, Void, List<MapPoints>>() {
                @Override
                protected List<MapPoints> doInBackground(Void... voids) {
                    mapPointRepository = new MapPointRepository(userDatabase.getInstance(getApplicationContext()).mapPointDao());
                    return mapPointRepository.getAllMapPoints();
                }

                @Override
                protected void onPostExecute(List<MapPoints> dividersList) {
                    // Now, you can access dividersList on the main thread

                    // Log the list of start locations
//                    for (int i = 0; i < startLocationsList.size(); i++) {
//                        LatLng latLng = startLocationsList.get(i);
//                        Log.d("StartLocation", "Step " + i + ": " + latLng.latitude + ", " + latLng.longitude);
//                    }

                  //  Toast.makeText(SetPathActivity.this, dividersList + "", Toast.LENGTH_SHORT).show();




                    for (MapPoints item : dividersList) {

                        if (startLocationsList.contains(new LatLng(item.latitude, item.longitude))) {


                            double delay = calculateDistance(startLocationsList.get(0).latitude,startLocationsList.get(0).longitude,item.latitude,item.longitude);
                            long delay1 = (long)delay;

                                progressDialog.show();
                                handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Call your function or perform any operation after the delay
                                        // For example, you can call yourDelayedFunction();
                                        progressDialog.dismiss();
                                        Toast.makeText(SetPathActivity.this, "Divider detected "+delay1+" unit time", Toast.LENGTH_SHORT).show();
                                        new ConnectToRaspberryPiTask().execute();

                                        // Dismiss the loader

                                    }
                                }, delay1*1000);
                                break;

                                //Thread.sleep(delay1*1000);

                                //Toast.makeText(SetPathActivity.this, "Divider detected "+delay1, Toast.LENGTH_SHORT).show();
                              //  new ConnectToRaspberryPiTask().execute();


                            //Log.d("divider", "divider detected");
                        } else {
                            //  Toast.makeText(SetPathActivity.this, "NO Divider detected ", Toast.LENGTH_SHORT).show();
                           // Log.d("divider", "No divider detected");
                        }
                    }




                    Intent intent = new Intent(SetPathActivity.this, ShowRoute.class);
                    intent.putParcelableArrayListExtra("routePoints", new ArrayList<>(startLocationsList));
                    intent.putExtra("dividersList", new ArrayList<>(dividersList));
                    startActivity(intent);
                }
            }.execute();



        }

        private  double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            // Convert latitude and longitude from degrees to radians
            lat1 = Math.toRadians(lat1);
            lon1 = Math.toRadians(lon1);
            lat2 = Math.toRadians(lat2);
            lon2 = Math.toRadians(lon2);

            // Calculate the differences between latitudes and longitudes
            double dLat = lat2 - lat1;
            double dLon = lon2 - lon1;

            // Haversine formula
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // Distance in kilometers
            return 6371 * c;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks from the handler to prevent memory leaks
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    }



