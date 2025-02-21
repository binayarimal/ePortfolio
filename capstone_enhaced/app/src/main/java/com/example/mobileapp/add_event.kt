package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

class add_event : AppCompatActivity() {
    var eventDate: EditText? = null
    var eventData: EditText? = null
    var eventButton: Button? = null
    var db: databaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        eventButton = findViewById(R.id.eventButton)
        eventDate = findViewById(R.id.event_date)
        eventData = findViewById(R.id.event_data)
        db = databaseHelper()
        eventButton?.setOnClickListener(View.OnClickListener {

            val event = eventData?.getText().toString()
            val date = eventDate?.getText().toString()

            if (event.isEmpty() or date.isEmpty()) {
                Toast.makeText(this@add_event, "Empty event or date", Toast.LENGTH_LONG).show()
            } else if (!checkDate(date)) {
                Toast.makeText(
                    this@add_event,
                    "date format incorrect, Use: yyyy-mm-dd",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Add event to database if user input is valid
                db?.insertEventData(event, date) { success ->
                    if (success) {
                        println("Event added successfully!")
                        val intent4 = Intent(this@add_event, events::class.java)
                        this@add_event.startActivity(intent4)
                        Toast.makeText(this@add_event, "Event Added", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        println("Failed to add event.")
                        Log.e("Success", "success")
                        Toast.makeText(
                            this@add_event,
                            "Error occured while trying to add event",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    companion object {
        fun checkDate(date: String?): Boolean {
            //validating date
            val valid: Boolean
            valid = try {
                // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
                LocalDate.parse(
                    date,
                    DateTimeFormatter.ofPattern("uuuu/M/d")
                        .withResolverStyle(ResolverStyle.STRICT)
                )
                true
            } catch (e: DateTimeParseException) {
                e.printStackTrace()
                false
            }
            return valid
        }
    }
}