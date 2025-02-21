package com.example.mobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class  events extends AppCompatActivity {

    Button addEventButton;
    databaseHelper db, dbDel;


    public void deleteEvent(Integer eventId){
        dbDel = new databaseHelper(this);
        Boolean delVal = dbDel.deleteEvent(eventId);

        if(delVal){
            Toast.makeText(events.this, "Event Deleted", Toast.LENGTH_LONG).show();
            startActivity(getIntent());

        }else{
            Toast.makeText(events.this, "Event Deletion Unsuccessful", Toast.LENGTH_LONG).show();
        }

    }
    public void setDateAttr(TextView dateView, String eventDate){

        LayoutParams dateParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        //dynamically styling date column

        dateParams.gravity = Gravity.START;
        dateParams.height = 120;
        dateParams.width = 200;
        dateParams.setMargins(5,5,10,5);
        dateView.setLayoutParams(dateParams);
        dateView.setTextSize(12);
        dateView.setText(eventDate);
        dateView.setId(View.generateViewId());
    }
    public void setEventAttr(TextView eventView, String eventData){

        LayoutParams eventParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        //Dynamically Styling event column

        eventParams.gravity = Gravity.START;
        eventParams.height = 120;
        eventParams.width = 500;
        eventParams.setMargins(5,5,5,5);
        eventView.setLayoutParams(eventParams);
        eventView.setTextSize(15);
        eventView.setText(eventData);
        eventView.setId(View.generateViewId());
    }

    public void setDelBtnAttr(Button del){
        LayoutParams delParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        //Dynamically Styling delete button

        delParams.gravity = Gravity.START;
        delParams.height = 120;
        delParams.width = 150;
        delParams.setMargins(5,5,5,5);
        del.setLayoutParams(delParams);
        del.setTextSize(10);
        del.setText("X");
        del.setId(View.generateViewId());
        del.setBackgroundColor(Color.rgb(255, 69, 0));
        del.setTextColor(Color.WHITE);
    }
    public void setEditBtnAttr(Button edit){
        LayoutParams editParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        //Dynamically Styling edit button

        editParams.gravity = Gravity.START;
        editParams.height = 120;
        editParams.width =150;
        editParams.setMargins(5,5,5,5);
        edit.setLayoutParams(editParams);
        edit.setTextSize(10);
        edit.setText("EDIT");
        edit.setId(View.generateViewId());
        edit.setBackgroundColor(Color.rgb(70, 130, 180));
        edit.setTextColor(Color.WHITE);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        db = new databaseHelper(this);
        Cursor res = db.getEvents();


        LinearLayout myRoot = (LinearLayout) findViewById(R.id.linear_parent);
// iterating events in database
        while(res.moveToNext()){
            @SuppressLint("Range") String eventData = res.getString( res.getColumnIndex("event") );
            @SuppressLint("Range") Integer eventId = res.getInt(1);
            @SuppressLint("Range") String eventDate = res.getString( res.getColumnIndex("date") );

            TextView dateView = new TextView(this);
            setDateAttr(dateView, eventDate);

            TextView eventView = new TextView(this);
            setEventAttr(eventView, eventData);


            Button del = new Button(this);
            setDelBtnAttr(del);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteEvent(eventId);

                }
            });

            Button edit = new Button(this);
            setEditBtnAttr(edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editScreen = new Intent(getApplicationContext(), edit_event.class);
                    editScreen.putExtra("key",eventId);
                    startActivity(editScreen);
                }
            });

            //Assigning views to layout

            LinearLayout layoutChild = new LinearLayout(this);
            layoutChild.setOrientation(LinearLayout.HORIZONTAL);
            layoutChild.addView(dateView);
            layoutChild.addView(eventView);
            layoutChild.addView(edit);
            layoutChild.addView(del);

            myRoot.addView(layoutChild);
        }

        addEventButton =  findViewById(R.id.add_event_view);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            //navigate to add event page
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), add_event.class);
                startActivity(intent);
            }
        });

    }
}