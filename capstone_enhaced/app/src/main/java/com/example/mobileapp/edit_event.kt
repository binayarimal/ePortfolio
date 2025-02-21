package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class edit_event : AppCompatActivity() {
    var editDate: EditText? = null
    var editEvent: EditText? = null
    var editEventButton: Button? = null
    var db: databaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_edit_event)
        editEventButton = findViewById(R.id.editEventButton)
        editDate = findViewById(R.id.editDate)
        editEvent = findViewById(R.id.editEvent)
        val eventId = intent.getIntExtra("key", -1)


        //Update event in database
        db = databaseHelper()
        editEventButton?.setOnClickListener(View.OnClickListener {
            val event = editEvent?.getText().toString()
            val date = editDate?.getText().toString()
            val updated = db?.updateEvent(eventId.toString(), event, date);
            if (updated == true) {
                Toast.makeText(this@edit_event, "Update Successful", Toast.LENGTH_LONG).show()
                val intent = Intent(this@edit_event, events::class.java)
                this@edit_event.startActivity(intent)
            } else {
                Toast.makeText(this@edit_event, "Update Unsuccessful", Toast.LENGTH_LONG).show()
            }
        })
    }
}