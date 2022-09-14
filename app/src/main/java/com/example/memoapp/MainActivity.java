package com.example.memoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private String status;
    private double user_latitude;
    private double user_longitude;
    protected LocationManager locationManager;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MemoList list = MemoList.getInstance();

        //User position management
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //load MemoList from file
        Gson gson = new Gson();
        try {
            FileInputStream file = openFileInput("memoList");
            BufferedReader data = new BufferedReader(new InputStreamReader(file));
            String line = null;

            while ((line = data.readLine()) != null) {
                Memo c = gson.fromJson(line, Memo.class);
                list.addElement(c);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error while loading data", Toast.LENGTH_SHORT).show();
        }
        geofencingClient = LocationServices.getGeofencingClient(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        status = "active";
        if (status.equals("active")) {
            TextView textView = (TextView) findViewById(R.id.memo_status);
            textView.setText("Active memos");
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MemoAdapter());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5000, this);
        }
        //SET GEOFENCES
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            addGeofence();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                //Show a dialog and ask for permission if background location not given
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1002);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1002);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // save data to file
        try {
            FileOutputStream file = openFileOutput("memoList", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            MemoList instance = MemoList.getInstance();
            for (int i = 0; i < instance.size(); ++i) {
                String data = gson.toJson(instance.memoAtIndex(i)) + "\n";
                file.write(data.getBytes());
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Error while saving data", Toast.LENGTH_SHORT).show();
        }
        //remove location updates for saving battery
        locationManager.removeUpdates(this);

    }

    //USER POSITION MANAGEMENT
    @Override
    public void onLocationChanged(Location location) {
        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
        Log.d("LOCATION CHANGED",String.valueOf(user_latitude));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
            // Handle item selection in the menu
            switch (item.getItemId()) {
                case R.id.Info:
                    showInfoDialog();
                    return true;
                case R.id.expired_button:
                    showExpiredMemos();
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    //Launching add memo activity
    public void addButtonPressed(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    //Change memo views from active to complete and vice-versa
    public void viewCompletedPressed(View view) {
        if (status.equals("active")) {
            MemoAdapter m = new MemoAdapter();
            m.setStatus("Completed");
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setAdapter(m);
            TextView textView = (TextView) findViewById(R.id.memo_status);
            textView.setText("Completed memos");
            status = "complete";
        } else if (status.equals("complete")) {
            MemoAdapter m = new MemoAdapter();
            m.setStatus("active");
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setAdapter(m);
            TextView textView = (TextView) findViewById(R.id.memo_status);
            textView.setText("Active memos");
            status = "active";
        }
    }

    public void mapButtonPressed(View view) {
        MemoList memoList = MemoList.getInstance();
        ArrayList<LatLng> memos_coordinates = new ArrayList<>();
        ArrayList<String> memos_titles = new ArrayList<>();
        //get active memos data for map activity
        for (Memo m : memoList.getActiveMemos()) {
            if (!Utils.isExpired(Utils.currentDate(),m.getDay())) {
                String string_latlon = m.getLatlon();
                String[] string = string_latlon.split(",");
                String str_lat = string[0].substring(10);
                String str_lon = string[1].substring(0, string[1].length() - 1);
                double lat = Double.parseDouble(str_lat);
                double lon = Double.parseDouble(str_lon);
                memos_coordinates.add(new LatLng(lat, lon));
                memos_titles.add(m.getTitle());
            }
        }
        //send data to Map Activity
        Intent map_intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("memos_coordinates", memos_coordinates);
        map_intent.putExtras(bundle);
        map_intent.putExtra("memos_titles", memos_titles);
        map_intent.putExtra("user_double_lat", user_latitude);
        map_intent.putExtra("user_double_lon", user_longitude);
        startActivity(map_intent);

    }



    //GEOFENCE AND NOTIFICATION MANAGEMENT
    protected void addGeofence( ) {
        MemoList memoList = MemoList.getInstance();
        for (Memo m : memoList.getActiveMemos()) {
            if (!Utils.isExpired(Utils.currentDate(), m.getDay())) { //if not expired
                if (m.getNotification() == false) { //if memo doesn't have already a geofence
                    String string_latlon = m.getLatlon();
                    String[] string = string_latlon.split(",");
                    String str_lat = string[0].substring(10);
                    String str_lon = string[1].substring(0, string[1].length() - 1);
                    double lat = Double.parseDouble(str_lat);
                    double lon = Double.parseDouble(str_lon);
                    LatLng latLng = new LatLng(lat, lon);
                    geofenceHelper = new GeofenceHelper(this);
                    geofenceHelper.memo = m;
                    Geofence geofence = geofenceHelper.getGeofence(m.getTitle(), latLng, 150, Geofence.GEOFENCE_TRANSITION_ENTER);
                    GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
                    PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
                    checkPermission();
                    geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    m.setNotification(true);
                                    Log.d("UTIL.ADDGEOFENCE STATUS:", "GEOFENCE WITH REQUESTID " + geofence.getRequestId() + " ADDED");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String errorMessage = geofenceHelper.getErrorString(e);
                                    Log.d("UTIL.ADDGEOFENCE STATUS:", "addOnFailure: " + errorMessage);
                                }
                            });
                }
            }
        }
    }
    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permission to access Location not granted", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);

            return false;
        }
        return true;
    }
    //launch fragment for ''How to use'' dialog when 'question mark' menu botton is pressed
    private void showInfoDialog(){
        FragmentManager fm = getSupportFragmentManager();
        InfoFragment editNameDialogFragment = InfoFragment.newInstance("InfoFragment");
        editNameDialogFragment.show(fm, "info_fragment");

    }
    //change recyclerView to 'expired  memos'.
    private void showExpiredMemos(){
        MemoAdapter m = new MemoAdapter();
        m.setStatus("expired");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(m);
        TextView textView = (TextView) findViewById(R.id.memo_status);
        textView.setText("Expired memos");
        status = "complete"; //When user click on active/completed button it will show active memos
    }
}


