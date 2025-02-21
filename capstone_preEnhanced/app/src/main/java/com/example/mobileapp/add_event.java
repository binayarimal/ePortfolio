package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;


public class add_event extends AppCompatActivity {

    EditText  eventDate, eventData;
    Button eventButton;
    databaseHelper db;

    public static boolean checkDate(String date) {
        //validating date
        boolean valid;
        try {
            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu/M/d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );

            valid = true;

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        eventButton = findViewById(R.id.eventButton);
        eventDate = findViewById(R.id.event_date);
        eventData = findViewById(R.id.event_data);

        db = new databaseHelper(this);

        eventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String event = eventData.getText().toString();
                String date = eventDate.getText().toString();

                if (event.isEmpty() | date.isEmpty()){
                    Toast.makeText(add_event.this, "Empty event or date", Toast.LENGTH_LONG).show();

                }else if(!checkDate(date)) {
                    Toast.makeText(add_event.this, "date format incorrect, Use: yyyy-mm-dd", Toast.LENGTH_LONG).show();

                }else {
                    // Add event to database if user input is valid

                    Boolean findData = db.insertEventData(event, date);

                    if (findData) {
                        //Navigate to events page

                        Intent intent = new Intent(add_event.this, events.class);
                        add_event.this.startActivity(intent);

                        Toast.makeText(add_event.this, "Event Added", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(add_event.this, "Error occured while trying to add event", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


 

    }
}