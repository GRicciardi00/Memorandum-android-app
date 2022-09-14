package com.example.memoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    String title, desc, date, time, geofenceStr;
    NotificationHelper notificationHelper;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("TITLE");
        desc = intent.getStringExtra("DESC");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");
        geofenceStr = "";
        notificationHelper = new NotificationHelper(context);
        Intent i = new Intent(context, MainActivity.class);
        notificationHelper.sendHighPriorityNotification(title, geofenceStr+desc+" "+date+ " "+time , i);

    }


}
