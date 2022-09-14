package com.example.memoapp;


import static com.example.memoapp.MemoAdapter.ViewHolder.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class DetailActivity extends AppCompatActivity {
    private Button completedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        // retrieve data from the intent that started the activity
        String description = intent.getStringExtra(MEMO_DESCRIPTION);
        String title = intent.getStringExtra(MEMO_TITLE);
        String date = intent.getStringExtra(MEMO_DATE);
        String hour = intent.getStringExtra(MEMO_HOUR);
        String location = intent.getStringExtra(LOCATION);
        completedButton = findViewById(R.id.CompletetedButton);
        Memo m = MemoList.getInstance().getMemo(title,date,hour);
        //setting detail view
        if (m.getStatus().equals("active"))
        {
            completedButton.setText("SET TO COMPLETE");
        }
        else if (m.getStatus().equals("Completed")){
            completedButton.setText("SET TO ACTIVE");
            }
        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (m.getStatus().equals("active"))
                {
                    m.setStatus("Completed");
                    completedButton.setText("SET TO ACTIVE");
                }
                else if (m.getStatus().equals("Completed"))
                {
                    m.setStatus("active");
                    completedButton.setText("SET TO COMPLETE");
                }
        }});
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(title);
        TextView dateView = (TextView) findViewById(R.id.dateView);
        if(Utils.isExpired(Utils.currentDate(),date))
        {
            dateView.setTextColor(0xFFFF0000);
            dateView.setText("Expired on " + date);
        }
        else {
            dateView.setText(date + " - " + hour);
        }
        TextView locationView = (TextView) findViewById(R.id.locationView);
        locationView.setText(location);
        TextView descriptionView = (TextView) findViewById(R.id.descriptionView);
        descriptionView.setText(description);

    }


}