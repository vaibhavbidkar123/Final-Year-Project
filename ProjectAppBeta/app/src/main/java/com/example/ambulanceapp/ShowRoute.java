package com.example.ambulanceapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShowRoute extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Polyline polyline;
    private ArrayList<MapPoints> dividersList;

   // private MapPointRepository mapPointRepository;

    private List<LatLng> coordinates; // Your list of coordinates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);
        Intent intent = getIntent();

        coordinates = intent.getParcelableArrayListExtra("routePoints");
        dividersList = (ArrayList<MapPoints>) getIntent().getSerializableExtra("dividersList");

      //  String assetFilePath = "Indian_States.txt";

        // Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
      //  new GeoJsonParserTask().execute(assetFilePath);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Plot the polyline on the map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates.get(0), 10f));
        googleMap.addPolyline(new PolylineOptions().addAll(coordinates).color(Color.BLACK));
        this.googleMap = googleMap;
        // Add markers for source (first coordinate) and destination (last coordinate)
        addMarker(googleMap, "Source", coordinates.get(0));
        addMarker(googleMap, "Destination", coordinates.get(coordinates.size() - 1));
//        addDividerMarker(googleMap, "Divider 1", new LatLng(15.3757246, 73.9258352));
//        addDividerMarker(googleMap, "Divider 2", new LatLng(15.3733589, 74.0106969));
//        addDividerMarker(googleMap, "Divider 3", new LatLng(15.2982048, 73.97173699999999));



        for (MapPoints item : dividersList) {
           addDividerMarker(googleMap, item.title,new LatLng(item.latitude,item.longitude));
        }


    }
    // Method to add a marker
    private void addMarker(GoogleMap googleMap, String title, LatLng position) {
        googleMap.addMarker(new MarkerOptions().position(position).title(title));

    }
    private  void addDividerMarker(GoogleMap googleMap, String title, LatLng position){

        //condition added which will plot only on dividers which come in between the route 
        // if(coordinates.contains(position))
         googleMap.addMarker(new MarkerOptions().position(position).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

    }

    // Other lifecycle methods for the MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
