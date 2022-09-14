package com.example.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double user_lat;
    private double user_lon;
    private ArrayList<Marker> markers;
    private ArrayList<LatLng> coordinates;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        markers = new ArrayList<Marker>();
        Intent intent = getIntent();
        user_lat = intent.getDoubleExtra("user_double_lat", user_lat);
        user_lon = intent.getDoubleExtra("user_double_lon", user_lon);
        coordinates = intent.getParcelableArrayListExtra("memos_coordinates");
        titles = intent.getStringArrayListExtra("memos_titles");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //add markers for each active memo
        for (int i = 0; i < coordinates.size(); i++) {
            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(coordinates.get(i))
                    .title(titles.get(i))));
        }
        //display user location in the map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        LatLng latlng = new LatLng(user_lat, user_lon);
        //if receive latitude and longitude from main activity set camera to user position
        if (latlng.latitude != 0 && latlng.longitude != 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 7));
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        titles = intent.getStringArrayListExtra("memos_titles");
        //update markers on the map each time MapActivity is called
        for (Marker marker : markers){
            if(!titles.contains(marker.getTitle()))
                marker.remove();
        }
    }


}
