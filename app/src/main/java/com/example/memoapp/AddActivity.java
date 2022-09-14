package com.example.memoapp;

import com.google.android.gms.maps.model.*;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.util.Log;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.location.Address;
import android.location.Geocoder;
import android.content.Context;
import java.util.List;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private Button addDateButton;
    private Button addHourButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addDateButton = findViewById(R.id.addDateButton);
        addDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == addDateButton) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get((Calendar.YEAR));
                    int month = calendar.get((Calendar.MONTH));
                    int day = calendar.get((Calendar.DAY_OF_MONTH));
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String string_day = String.format("%02d", day);
                            String string_month = String.format("%02d", month+1);
                            addDateButton.setText(string_day + "/" + string_month + "/" + year);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });
        addHourButton = findViewById(R.id.addHourButton);
        addHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v== addHourButton){
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                            addHourButton.setText(hours + ":" + minutes);
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                }
            }
        });
    }

    // used to save the new memo and verify the data integrity of the newly created memo
    public void saveInput(View view) {
        EditText locationInput = (EditText)findViewById(R.id.locationInput);
        EditText titleInput = (EditText)findViewById(R.id.titleInput);
        EditText descriptionInput = (EditText)findViewById(R.id.descriptionInput);
        String title = titleInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String location = locationInput.getText().toString();
        String date = addDateButton.getText().toString();
        String hour = addHourButton.getText().toString();
        String latlon = getLocationFromAddress(getApplicationContext(), location);
        Log.d("LATLON", latlon);
        //check is values are missing
        if(description.equals("") || title.equals("") || date.equals("Add date") || hour.equals("Add hour") || location.equals("")) {
            Toast errorMessage = Toast.makeText(getApplicationContext(), "Missing info", Toast.LENGTH_SHORT);
            errorMessage.show();
            TextView descriptionView = findViewById(R.id.DescriptionTextView);
            TextView titleView = findViewById(R.id.TitleTextView);
            TextView dateTextView = findViewById(R.id.expirationTextView);
            TextView hourTextView = findViewById(R.id.hourTextView);
            TextView locationTextView = findViewById(R.id.locationTextView);
            if(title.equals(""))
                titleView.setTextColor(0xFFFF0000);
            if(description.equals(""))
                descriptionView.setTextColor(0xFFFF0000);
            if(location.equals(""))
                locationTextView.setTextColor(0xFFFF0000);
            if(date.equals("Add date"))
                dateTextView.setTextColor(0xFFFF0000);
            if(hour.equals("Add hour"))
                hourTextView.setTextColor(0xFFFF0000);
        }
        else if(latlon.equals("not found"))
        {
            Toast errorMessage = Toast.makeText(getApplicationContext(), "Insert a valid location", Toast.LENGTH_SHORT);
            errorMessage.show();
        }
        else if (Utils.isExpired(Utils.currentDate(),date)==true){
            Toast errorMessage = Toast.makeText(getApplicationContext(), "Insert a valid date", Toast.LENGTH_SHORT);
            errorMessage.show();
        }
        else {
            Memo c;
            c = new Memo(title, description, date, hour,location,latlon);
            MemoList.getInstance().addElement(c);
            finish();

        }

    }
    //Geocoding location from string to LatLon
    public String getLocationFromAddress(Context context,String strAddress){
        Geocoder coder = new Geocoder(context);
        Log.d("Address", strAddress);
        List<Address> addresses;
        String LatLan= "";
            try {
                if(!strAddress.isEmpty()) {
                    // May throw an Exception
                    addresses = coder.getFromLocationName(strAddress, 5);
                    Address bestMatch = (addresses.isEmpty() ? null : addresses.get(0));
                    if (bestMatch != null) {
                        LatLan = new LatLng(bestMatch.getLatitude(), bestMatch.getLongitude()).toString();
                    } //if invalid input location return nof found
                    else {
                        LatLan = "not found";
                    }
                }
                //if empty return ""
                else {
                    LatLan = "";
                }
            }//for other exceptions throw e
            catch (Exception e){
            Toast.makeText(this, "Error search :- Wrong input", Toast.LENGTH_LONG).show();
        }
        return LatLan;
    }
}