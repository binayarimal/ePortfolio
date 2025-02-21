package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class edit_event extends AppCompatActivity {

    EditText editDate, editEvent;
    Button editEventButton;
    databaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_event);

        editEventButton = findViewById(R.id.editEventButton);
        editDate = findViewById(R.id.editDate);
        editEvent = findViewById(R.id.editEvent);
        Integer eventId = getIntent().getIntExtra("key", -1);


        //Update event in database

        db = new databaseHelper(this);

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event = editEvent.getText().toString();
                String date = editDate.getText().toString();

                Boolean updated = db.updateEvent(eventId,event,date);

                if(updated){
                    Toast.makeText(edit_event.this, "Update Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(edit_event.this, events.class);
                    edit_event.this.startActivity(intent);
                }else{
                    Toast.makeText(edit_event.this, "Update Unsuccessful", Toast.LENGTH_LONG).show();
                }
            }


        });




    }
}