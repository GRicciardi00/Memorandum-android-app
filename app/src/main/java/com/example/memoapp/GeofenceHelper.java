package com.example.memoapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper { //ContextWrapper - to use the context using the keyword inside the class

    PendingIntent pendingIntent;

    static int count = 0;
    public Memo memo;

    private String TAG = "GeofenceHelper";

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence){

        return  new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    public Geofence getGeofence(String ID, LatLng latLng, float radius, int transitionTypes){

        return  new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public PendingIntent getPendingIntent(){
        if(pendingIntent != null)
            return  pendingIntent;
        //Log.e(TAG,  "" + memo.getMemoId());
        Intent notificationIntent = new Intent(this, NotificationBroadcastReceiver.class);
        notificationIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        notificationIntent.putExtra("TITLE", memo.getTitle());
        notificationIntent.putExtra("DESC", memo.getDescription());
        notificationIntent.putExtra("DATE", memo.getDay());
        notificationIntent.putExtra("TIME", memo.getHour());

        pendingIntent = PendingIntent.getBroadcast(this,count++, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return  pendingIntent;
    }

    public String getErrorString(Exception e){
        if (e instanceof ApiException){
            ApiException apiException = (ApiException)  e;
            switch (apiException.getStatusCode()){
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            }
        }
        return  e.getLocalizedMessage();
    }
}
